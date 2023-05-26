package edu.unipd.dei.eis;

import java.util.List;
import edu.unipd.dei.eis.TermsStore.TermsStore;
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

    /**
     * Costruttore
     * 
     * @param ts Un oggetto TermsStore che si occupa di memorizzare i termini
     */
    public ExtractionManager(TermsStore ts) {
        this.ts = ts;
    }

    /**
     * Processa una lista di articoli
     * 
     * @param articles La lista di articoli da processare
     */
    public void process(List<Article> articles) {
        try (ProgressBar pb = new ProgressBarBuilder()
            .setTaskName("Processing articles")
            .setInitialMax(articles.size())
            .setUpdateIntervalMillis(250)
            .showSpeed()
            .setUnit(" articles", 1)
            .build()) {
            long startTime = System.currentTimeMillis();
            for (Article a : articles) {
                ts.registerArticleTerms(te.extractTerms(a));
                pb.step();
            }
            logger.info("Processed {} articles in {} ms", articles.size(),
                System.currentTimeMillis() - startTime);
        }

        new TrueCaseHeuristic(ts).processCase();
    }
}
