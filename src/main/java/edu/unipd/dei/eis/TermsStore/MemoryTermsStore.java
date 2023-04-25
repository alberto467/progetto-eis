package edu.unipd.dei.eis.TermsStore;

import java.util.HashMap;
import java.util.Map;

public class MemoryTermsStore implements TermsStore {
    private Map<String, Integer> terms = new HashMap<String, Integer>();

    public void registerArticleTerms(Iterable<String> terms) {
        for (String term : terms) {
            if (this.terms.containsKey(term))
                this.terms.put(term, this.terms.get(term) + 1);
            else
                this.terms.put(term, 1);
        }
    }

    public Map<String, Integer> getTerms() {
        return terms;
    }

    public void clear() {
        terms.clear();
    }
}
