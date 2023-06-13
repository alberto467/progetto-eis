package edu.unipd.dei.eis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Questa classe si occupa di processare il testo di un articolo per estrarre i termini e il
 * relativo numero di occorrenze.
 */
public class TermsExtractor {
    private static final Logger logger = LoggerFactory.getLogger(TermsExtractor.class);
    StanfordCoreNLP pipeline;

    /**
     * Costruttore vuoto
     */
    public TermsExtractor() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, pos, lemma, stopword");
        props.setProperty("tokenize.options",
            "americanize=false, normalizeParentheses=false, untokenizable=noneDelete");
        props.setProperty("customAnnotatorClass.stopword", "edu.unipd.dei.eis.StopWordAnnotator");
        props.setProperty("ssplit.isOneSentence", "true");

        // NOTE: CoreNLP's truecase annotator causes consistent crashes, probably on loading the
        // truecase model due to bugs or memory constraints. A max heap size of 4G was tested with a
        // very short phrase and still produced consistent crashes. A true-case heuristic will be
        // used instead, for more info checkout the TrueCaseHeuristic class.

        pipeline = new StanfordCoreNLP(props);
    }

    private static boolean isValidWord(String word) {
        // Previene singole lettere
        if (word.length() <= 1)
            return false;

        // Previene token contenenti cifre numeriche
        if (word.matches(".*\\d.*"))
            return false;

        // Previene token come "'m" e simili
        if (!word.matches(".*[a-zA-Z]{2}.*"))
            return false;

        // Previene social media handles
        if (word.startsWith("@"))
            return false;

        return true;
    }

    // Lista di tag da escludere, in formato Penn Treebank
    // (https://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html)
    private static Set<String> tagsToExclude =
        new HashSet<>(Arrays.asList("CC", "CD", "IN", "DT", "PRP", "PRP$", "PDT", "WDT", "WP",
            "WP$", "TO", "EX", "LS", "POS", "RP", "WRB", "UH", "MD", "RB", "RBR", "RBS"));

    private static boolean isValidTag(String tag) {
        return !tagsToExclude.contains(tag);
    }

    private static boolean isValidToken(CoreLabel token) {
        if (token.get(StopWordAnnotator.class))
            return false;

        if (!isValidTag(token.tag()))
            return false;

        if (!isValidWord(token.lemma()))
            return false;

        return true;
    }

    /**
     * Estrae i termini da un articolo
     * 
     * @param article L'articolo da cui estrarre i termini
     * 
     * @return Un insieme di termini
     */
    public Set<String> extractTerms(Article article) {
        logger.trace("Extracting terms from article {}", article.id);
        long startTime = System.currentTimeMillis();

        CoreDocument document = new CoreDocument(article.title + '\n' + article.body);

        pipeline.annotate(document);

        Set<String> terms = new HashSet<>();
        int tokenCount = 0;
        for (CoreLabel token : document.tokens()) {
            tokenCount++;

            if (terms.contains(token.lemma()))
                continue;

            if (isValidToken(token))
                terms.add(processWord(token.lemma()));
        }

        long elapsedMs = System.currentTimeMillis() - startTime;
        String tokensPerSecond = String.format("%.2f", tokenCount / (elapsedMs / 1000.0));
        logger.trace("Extracted {} terms from article {} of {} tokens chars in {} ms ({} tokens/s)",
            terms.size(), article.id, tokenCount, elapsedMs, tokensPerSecond);

        return terms;
    }

    private static String processWord(String token) {
        // Rimuove caratteri non ASCII
        return token.replaceAll("[^\\x00-\\x7F]", "");
    }
}
