package edu.unipd.dei.eis;

import java.io.File;

import edu.unipd.dei.eis.CLI.ExtractCommand;

import org.junit.jupiter.api.Test;

class ExtractionTest extends ExtractCommand
{
    private static final File inputArticles = new File("src/test/resources/test_storage/articles");
    private static final File outputTerms = new File("src/test/resources/test_storage/terms");
    private static final File outputResults = new File("src/test/resources/test_storage/results/test4_results.txt");
    private Integer threads = null;

    @Test
    void testextract() throws Exception
    {
        new ExtractCommand(inputArticles, outputTerms, outputResults, threads).task();
    }
}