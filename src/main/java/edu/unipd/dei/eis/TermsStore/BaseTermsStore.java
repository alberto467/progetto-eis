package edu.unipd.dei.eis.TermsStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseTermsStore implements TermsStore {
    protected Map<String, Integer> terms = new HashMap<String, Integer>();

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

    public List<Map.Entry<String, Integer>> getTopTerms(Integer limit) {
        return terms.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .sorted((a, b) -> b.getValue() - a.getValue()).limit(limit)
                .collect(Collectors.toList());
    }

    public void mergeTerms(String targetTerm, Set<String> otherTerms) {
        int sum = terms.get(targetTerm);

        for (String t : otherTerms)
            sum += terms.remove(t);

        terms.put(targetTerm, sum);
    }
}
