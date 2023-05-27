package edu.unipd.dei.eis.CLI;

import java.io.File;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "download-extract")
public class DownloadExtractCommand extends BaseCommand {
    @Option(names = {"-s", "--sources"}, required = true,
        defaultValue = "all", description = "The sources to download from")
    private String[] sources;

    @Option(names = {"-n", "--number"},
        description = "The number of articles to download from each source")
    private int number = 10;

    @Option(names = {"-oa", "--output-articles"}, defaultValue = "tmp/articles",
        description = "The output directory for articles files")
    private File outputArticles;

    @Option(names = {"-ot", "--output-terms"}, defaultValue = "tmp/terms",
        description = "The output directory for terms files")
    private File outputTerms;

    @Option(names = {"-or", "--output-results"}, defaultValue = "tmp/top_terms.txt",
        description = "The output path for the top terms file")
    private File outputResults;

    @Option(names = {"-t", "--threads"}, description = "Threads used for nlp processing")
    private Integer threads = null;

    @Override
    public void task() throws Exception {
        new DownloadCommand(sources, outputArticles, number).task();
        new ExtractCommand(outputArticles, outputTerms, outputResults, threads).task();
    }
}
