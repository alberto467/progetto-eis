package edu.unipd.dei.eis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.unipd.dei.eis.TermsStore.TermsStore;

/**
 * Performs heuristics to try and determine the true case of terms and avoids differently-cased
 * duplicate terms.
 */
public class TrueCaseHeuristic {
    private TermsStore ts;

    /**
     * @param ts The terms store to operate on.
     */
    public TrueCaseHeuristic(TermsStore ts) {
        this.ts = ts;
    }

    private Map<String, Set<String>> buildCaseMap() {
        Map<String, Set<String>> caseMap = new HashMap<>();

        for (String t : ts.getTerms().keySet()) {
            String lower = t.toLowerCase();
            if (caseMap.containsKey(lower)) {
                caseMap.get(lower).add(t);
            } else {
                Set<String> nSet = new HashSet<>();
                nSet.add(t);
                caseMap.put(lower, nSet);
            }
        }

        return caseMap;
    }

    void processCase() {
        for (Set<String> v : buildCaseMap().values()) {
            // If we have a single capitalized version of the token, we cannot make any inference
            // about it's true case.
            if (v.size() <= 1)
                continue;

            // Choose lowest string out of set (one with most lower-case letters)
            String target = v.stream().max(Comparator.comparing((String x) -> x)).get();
            v.remove(target);

            ts.mergeTerms(target, v);
        }
    }
}
