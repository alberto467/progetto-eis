package edu.unipd.dei.eis.TermsStore;

import java.util.Map;
import java.util.Set;
import edu.unipd.dei.eis.TopTerms;

public interface TermsStore {
    public void registerArticleTerms(Iterable<String> terms);

    public Map<String, Integer> getTerms();

    public void clear();

    public TopTerms getTopTerms(Integer limit);

    public void save(String id) throws Exception;

    public void load(String id) throws Exception;

    public void mergeTerms(String targetTerm, Set<String> otherTerms);
}
