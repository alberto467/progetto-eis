package edu.unipd.dei.eis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import org.junit.jupiter.api.Test;

class TermsExtractorTest {
    private StanfordCoreNLP pipeline;
    private static final File inputFile =
        new File("src/test/resources/test_source/TermsExtractorTest/input_test.txt");
    private static final String outputFilePath =
        "src/test/resources/test_storage/TermsExtractorTest/output_test.txt";
    private static final File outputFile = new File(outputFilePath);
    private static final File outputFolder =
        new File("src/test/resources/test_storage/TermsExtractorTest");

    @Test
    void testExtractTerms() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, pos, lemma, stopword");
        props.setProperty("tokenize.options", "americanize=false");
        props.setProperty("customAnnotatorClass.stopword", "edu.unipd.dei.eis.StopWordAnnotator");
        props.setProperty("ssplit.isOneSentence", "true");

        pipeline = new StanfordCoreNLP(props);

        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Il file specificato non esiste");
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore durante la lettura del file: " + e.getMessage(), e);
        }

        String fileContent = contentBuilder.toString();
        long startTime = System.currentTimeMillis();

        CoreDocument document = new CoreDocument(fileContent);

        pipeline.annotate(document);

        Set<String> terms = new HashSet<>();
        int tokenCount = 0;
        for (CoreLabel token : document.tokens()) {
            tokenCount++;

            if (terms.contains(token.lemma()))
                continue;

            if (isValidToken(token))
                terms.add(token.lemma());
        }

        long elapsedMs = System.currentTimeMillis() - startTime;
        String tokensPerSecond = String.format("%.2f", tokenCount / (elapsedMs / 1000.0));

        // Controlla l'esistenza del file
        if (!outputFile.exists()) {
            try {
                // Crea il file di output
                outputFolder.mkdirs();
                outputFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(
                    "Errore durante la creazione del file di output: " + e.getMessage(), e);
            }
        }

        // Salva i termini estratti nel file di output
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            for (String term : terms) {
                writer.write(term + "\n");
            }
            writer.write("Tempo trascorso estrazione termini: " + (double) (elapsedMs / 1000.0)
                + "s\nToken al secondo: " + tokensPerSecond);
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file di output: " + e.getMessage());
        }
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

    private static Set<String> tagsToExclude =
        new HashSet<>(Arrays.asList("CC", "CD", "IN", "DT", "PRP", "PRP$",
            "PDT", "WDT", "WP", "WP$", "TO", "EX", "LS", "POS", "RP", "WRB", "UH", "MD", "RB",
            "RBR", "RBS"));

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
}
