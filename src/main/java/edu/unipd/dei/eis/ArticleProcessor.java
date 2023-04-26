package edu.unipd.dei.eis;

import java.util.List;
import java.util.Map;
import edu.unipd.dei.eis.TermsStore.TermsStore;

/**
 * Classe che si occupa di processare gli articoli
 */
public class ArticleProcessor {
    private TermsStore ts;
    private TermsExtractor te = new TermsExtractor();

    /**
     * Costruttore
     * 
     * @param ts Un oggetto TermsStore che si occupa di memorizzare i termini
     */
    public ArticleProcessor(TermsStore ts) {
        this.ts = ts;
    }

    /**
     * Restituisce la mappa dei termini
     * 
     * @return La mappa dei termini
     */
    public Map<String, Integer> getTerms() {
        return ts.getTerms();
    }

    /**
     * Cancella i termini memorizzati
     */
    public void clear() {
        ts.getTerms().clear();
    }

    /**
     * Restituisce i termini più frequenti
     * 
     * @param limit Il numero massimo di termini da restituire
     * 
     * @return I termini più frequenti
     */
    public List<Map.Entry<String, Integer>> getTopTerms(Integer limit) {
        return ts.getTopTerms(limit);
    }

    /**
     * Processa una lista di articoli
     * 
     * @param articles La lista di articoli da processare
     */
    public void process(Iterable<Article> articles) {
        for (Article a : articles) {
            System.out.println(a.title);
            ts.registerArticleTerms(te.extractTerms(a));
        }
    }
}
