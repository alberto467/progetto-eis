package edu.unipd.dei.eis;

import static org.junit.Assert.assertEquals;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;

class TopTermsTest {
    private List<Map.Entry<String, Integer>> terms = new ArrayList<>();
    private static final File inputFolder = new File("src/test/resources/test_source/TopTermsTest");
    private static final File output =
        new File("src/test/resources/test_storage/TopTermsTest/test_results.txt");
    private static final File outputFolder =
        new File("src/test/resources/test_storage/TopTermsTest");
    private static final File comparison =
        new File("src/test/resources/test_source/TopTermsTest/expected_results.txt");

    @Test
    void testTopTerms() throws Exception {
        File[] input = inputFolder.listFiles((dir, name) -> name.endsWith(".json"));// cerca file
                                                                                    // .json nella
                                                                                    // cartella
                                                                                    // folder
        if (input != null) {
            FileTermsStore articles = new FileTermsStore(inputFolder);
            articles.load(input[0].getName().substring(0, input[0].getName().lastIndexOf('.')));
            for (Map.Entry<String, Integer> entry : terms) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                articles.getTerms().put(key, value);
            }

            // seleziona solo i 50 termini con il peso maggiore
            terms = (articles.getTerms().entrySet().stream()
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .sorted((a, b) -> b.getValue() - a.getValue()).limit(50)
                .collect(Collectors.toList()));

            if (!output.exists()) {
                // Crea il file di output
                outputFolder.mkdirs();
                output.createNewFile();
            }

            write(output);// test della funzione write

            String alpha = new String(Files.readAllBytes(output.toPath()));
            String beta = new String(Files.readAllBytes(comparison.toPath()));
            assertEquals(beta.length(), alpha.length());
            assertEquals(beta, alpha);
            if (!alpha.equals(beta))// confronta il risultato di questo test con il risultato
                                    // previsto
            {
                Assertions.fail("I risultati previsti sono diversi");
            }
        } else
            Assertions.fail(
                "Non c'è nessun file .json nella cartella src/test/resources/test_source/TopTermsTest");

    }

    private void write(File path) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        getPrintStream().forEachOrdered(s -> {
            try {
                bw.write(s);
                bw.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        bw.close();
    }

    private Stream<String> getPrintStream() {
        Stream<String> out =
            terms.stream().map(e -> e.getKey().toString() + " " + e.getValue().toString());

        return out;
    }

    @Override
    public String toString() {
        return getPrintStream().reduce("", (a, b) -> a + b + "\n");
    }
}
