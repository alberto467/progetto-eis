package edu.unipd.dei.eis.TermsStore;

import java.util.List;
import java.util.Map;

public interface TermsStore {
    public void registerArticleTerms(Iterable<String> terms);

    public Map<String, Integer> getTerms();

    public void clear();

    public List<Map.Entry<String, Integer>> getTopTerms(Integer limit);

    public void save(String id) throws Exception;

    public void load(String id) throws Exception;
}
