package edu.unipd.dei.eis;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.io.File;
import edu.unipd.dei.eis.CLI.ExtractCommand;
import org.junit.jupiter.api.Test;

class ExtractionTest {
    private static final File inputArticles =
        new File("src/test/resources/test_source/ExtractionTest");
    private static final File outputTerms =
        new File("src/test/resources/test_storage/ExtractionTest");
    private static final File outputResults =
        new File("src/test/resources/test_storage/ExtractionTest/test_results.txt");
    private Integer threads = null;

    @Test
    void testExtract() {
        assertDoesNotThrow(() -> {
            new ExtractCommand(inputArticles, outputTerms, outputResults, threads).task();
        });
    }
}
