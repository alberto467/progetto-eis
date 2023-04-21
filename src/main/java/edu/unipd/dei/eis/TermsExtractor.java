package edu.unipd.dei.eis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/*
 * Questa classe si occupa di processare il testo di un articolo per estrarre i termini e il
 * relativo numero di occorrenze.
 */
public class TermsExtractor {
  StanfordCoreNLP pipeline;

  TermsExtractor() {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, cleanxml, ssplit, pos, lemma, stopword");
    props.setProperty("tokenize.options", "americanize=false");
    props.setProperty("customAnnotatorClass.stopword",
        "edu.unipd.dei.eis.StopWordAnnotator");
    props.setProperty("ssplit.isOneSentence", "true");

    // Truecase requires loading a model without maven
    // props.setProperty("truecase.overwriteText", "true");
    // props.setProperty("truecase.verbose", "true");

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
      new HashSet<>(Arrays.asList("CC", "CD", "IN", "DT",
          "PRP", "PRP$", "PDT", "WDT", "WP", "WP$", "TO", "EX", "LS", "POS", "RP", "WRB", "UH",
          "MD"));

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

  public HashMap<String, Integer> extractTerms(String text) {
    // text = text.toLowerCase();
    CoreDocument document = pipeline.processToCoreDocument(text);

    HashMap<String, Integer> terms = new HashMap<String, Integer>();
    for (CoreLabel token : document.tokens()) {
      String word = token.lemma();

      if (terms.containsKey(word))
        terms.put(word, terms.get(word) + 1);
      else if (isValidToken(token))
        terms.put(word, 1);
    }
    return terms;
  }
}
