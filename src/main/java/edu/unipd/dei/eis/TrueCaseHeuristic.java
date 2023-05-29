package edu.unipd.dei.eis;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Performs heuristics to try and determine the true case of terms and avoids differently-cased
 * duplicate terms.
 */
public class TrueCaseHeuristic {
    private Map<String, Integer> terms;

    /**
     * Constructor
     */
    public TrueCaseHeuristic(Map<String, Integer> terms) {
        this.terms = terms;
    }

    private Map<String, SortedSet<String>> buildCaseMap() {
        // Map of lower-case version of terms to set of all differently-cased versions of the term
        Map<String, SortedSet<String>> caseMap = new HashMap<>();

        for (String t : terms.keySet()) {
            String lower = t.toLowerCase();
            if (caseMap.containsKey(lower)) {
                caseMap.get(lower).add(t);
            } else {
                SortedSet<String> nSet = new TreeSet<>();
                nSet.add(t);
                caseMap.put(lower, nSet);
            }
        }

        return caseMap;
    }

    void processCase() {
        for (SortedSet<String> v : buildCaseMap().values()) {
            // If we have a single capitalized version of the token, we cannot make any inference
            // about it's true case.
            if (v.size() <= 1)
                continue;

            // Choose lowest string out of set (one with most lower-case letters)
            String target = v.last();
            v.remove(target);

            int sum = terms.get(target);
            sum += v.stream()
                .map(s -> terms.remove(s))
                .reduce(0, (a, b) -> a + b);

            terms.put(target, sum);
        }
    }
}
