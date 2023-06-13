package edu.unipd.dei.eis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Performs heuristics to try and determine the true case of terms and avoids differently-cased
 * duplicate terms.
 */
public class TrueCaseHeuristic {

    /**
     * Constructor
     */
    public TrueCaseHeuristic() {}

    static private Map<String, String> buildCaseMap(List<Set<String>> termsSets) {
        // Map of lower-case version of terms to set of all differently-cased versions of the term
        Map<String, SortedSet<String>> caseSets = new HashMap<>();

        for (Set<String> terms : termsSets)
            for (String term : terms) {
                String lower = term.toLowerCase();

                SortedSet<String> s = caseSets.get(lower);

                if (s == null) {
                    s = new TreeSet<>();
                    caseSets.put(lower, s);
                }

                s.add(term);
            }

        Map<String, String> caseMap = new HashMap<>();

        for (Map.Entry<String, SortedSet<String>> e : caseSets.entrySet()) {
            if (e.getValue().last().equals(e.getKey()))
                continue;

            caseMap.put(e.getKey(), e.getValue().last());
        }

        return caseMap;
    }

    static void processCase(List<Set<String>> termsSets) {
        Map<String, String> caseMap = buildCaseMap(termsSets);

        termsSets.forEach(terms -> {
            List<String> toRemove = new ArrayList<>();
            List<String> toAdd = new ArrayList<>();

            for (String term : terms) {
                String lc = term.toLowerCase();
                if (term == lc)
                    continue;

                String corrected = caseMap.get(lc);

                if (term == corrected)
                    continue;

                toRemove.add(term);

                if (corrected == null)
                    toAdd.add(term.toLowerCase());
                else
                    toAdd.add(corrected);
            }

            toRemove.forEach(terms::remove);
            terms.addAll(toAdd);
        });
    }
}
