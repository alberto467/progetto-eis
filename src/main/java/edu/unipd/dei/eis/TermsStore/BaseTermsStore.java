package edu.unipd.dei.eis.TermsStore;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import edu.unipd.dei.eis.TopTerms;

public abstract class BaseTermsStore implements TermsStore {
    protected Map<String, Integer> terms = new ConcurrentHashMap<String, Integer>();

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

    public TopTerms getTopTerms(Integer limit) {
        List<Map.Entry<String, Integer>> list =
            (terms.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .sorted((a, b) -> b.getValue() - a.getValue()).limit(limit)
                .collect(Collectors.toList()));

        return new TopTerms(list);
    }
}
