package edu.unipd.dei.eis.CLI;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import edu.unipd.dei.eis.Article;
import edu.unipd.dei.eis.ExtractionManager;
import edu.unipd.dei.eis.TopTerms;
import edu.unipd.dei.eis.ArticleStorage.ArticleFileStore;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;
import java.io.File;
import java.sql.Timestamp;
import java.time.Instant;


@Command(name = "extract")
public class ExtractCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(ExtractCommand.class);

    @Option(names = {"-i", "--input"}, defaultValue = "tmp/articles",
        description = "The input directory")
    private File input;

    @Option(names = {"-ot", "--output-terms"}, defaultValue = "tmp/terms",
        description = "The output directory for terms")
    private File outputTerms;

    @Option(names = {"-or", "--output-results"}, defaultValue = "tmp/top_terms.txt",
        description = "The output path for the top terms file")
    private File outputResults;

    @Option(names = {"-t", "--threads"}, description = "Threads used for nlp processing")
    private Integer threads = null;

    public ExtractCommand() {}

    public ExtractCommand(File input, File outputTerms, File outputResults, Integer threads) {
        this.input = input;
        this.outputTerms = outputTerms;
        this.outputResults = outputResults;
        this.threads = threads;
    }

    public void task() throws Exception {
        if (input.exists() && !input.isDirectory())
            throw new RuntimeException("Input is not a directory");

        if (outputTerms.exists() && !outputTerms.isDirectory())
            throw new RuntimeException("Output terms is not a directory");

        if (outputResults.exists() && outputResults.isDirectory())
            throw new RuntimeException("Output results is not a file");

        if (threads == null) {
            threads = Runtime.getRuntime().availableProcessors();
            logger.info("Threads not specified, using {} threads", threads);
        }

        FileTermsStore termsStore = new FileTermsStore(outputTerms);
        ExtractionManager ap = new ExtractionManager(termsStore, threads);
        ArticleStorage fileStorage = new ArticleFileStore(input);

        // Iterable<Article> articles = downloadArticles();
        List<Article> articles = fileStorage.getAllArticles();
        ap.process(articles);
        termsStore.save(Timestamp.from(Instant.now()).toString());

        TopTerms topTerms = termsStore.getTopTerms(50);
        topTerms.write(outputResults);
        topTerms.print();
    }
}
