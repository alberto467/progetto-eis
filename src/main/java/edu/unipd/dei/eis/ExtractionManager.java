package edu.unipd.dei.eis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import edu.unipd.dei.eis.TermsStore.TermsStore;
import me.tongfei.progressbar.ConsoleProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe che si occupa di processare gli articoli
 */
public class ExtractionManager {
    private static final Logger logger = LoggerFactory.getLogger(TermsExtractor.class);
    private TermsStore ts;
    private TermsExtractor te = new TermsExtractor();
    private int threads;

    /**
     * Costruttore
     * 
     * @param ts Un oggetto TermsStore che si occupa di memorizzare i termini
     */
    public ExtractionManager(TermsStore ts, int threads) {
        this.ts = ts;
        this.threads = threads;
    }

    /**
     * Processa una lista di articoli
     * 
     * @param articles La lista di articoli da processare
     */
    public void process(List<Article> articles) throws Exception {
        long startTime = System.currentTimeMillis();

        logger.info("Processing {} articles...", articles.size());

        List<Set<String>> termsSets = null;

        try (ProgressBar pb = new ProgressBarBuilder()
            .setTaskName("Processing articles")
            .setInitialMax(articles.size())
            .setUpdateIntervalMillis(250)
            .showSpeed()
            .setUnit(" articles", 1)
            .setConsumer(new ConsoleProgressBarConsumer(System.out))
            .build()) {

            ForkJoinPool pool = new ForkJoinPool(threads);
            try {
                termsSets = pool.submit(() -> articles.parallelStream().map(a -> {
                    Set<String> terms = te.extractTerms(a);
                    pb.step();
                    return terms;
                })).get().collect(Collectors.toList());
            } finally {
                pool.shutdown();
                if (!pool.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    logger.warn("Thread pool did not terminate in time, forcing shutdown");
                    pool.shutdownNow();
                }
            }
        }

        TrueCaseHeuristic.processCase(termsSets);
        termsSets.forEach(ts::registerArticleTerms);

        logger.info("Processed {} articles in {} ms", articles.size(),
            System.currentTimeMillis() - startTime);
    }
}
