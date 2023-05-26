package edu.unipd.dei.eis;

import java.util.List;
import edu.unipd.dei.eis.TermsStore.TermsStore;
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
        long startTime = System.currentTimeMillis();
        for (Article a : articles) {
            ts.registerArticleTerms(te.extractTerms(a));
        }
        logger.info("Processed {} articles in {} ms", articles.size(),
            System.currentTimeMillis() - startTime);

        new TrueCaseHeuristic(ts).processCase();
    }
}
