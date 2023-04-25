package edu.unipd.dei.eis.TermsStore;

import java.util.Map;

public interface TermsStore {
    public void registerArticleTerms(Iterable<String> terms);

    public Map<String, Integer> getTerms();

    public void clear();
}
