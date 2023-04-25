package edu.unipd.dei.eis;

import java.util.Map;
import edu.unipd.dei.eis.TermsStore.TermsStore;

public class ArticleProcessor {
    private TermsStore ts;
    private TermsExtractor te = new TermsExtractor();

    public ArticleProcessor(TermsStore ts) {
        this.ts = ts;
    }

    public Map<String, Integer> getTerms() {
        return ts.getTerms();
    }

    public void clearTerms() {
        ts.getTerms().clear();
    }

    public void process(Iterable<Article> articles) {
        for (Article a : articles) {
            System.out.println(a.title);
            ts.registerArticleTerms(te.extractTerms(a));
        }
    }
}
