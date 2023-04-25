package edu.unipd.dei.eis.TermsStore;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {
    public static List<Entry<String, Integer>> getTopTerms(Map<String, Integer> terms, int num) {
        return terms.entrySet().stream().sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                .sorted((a, b) -> b.getValue() - a.getValue()).limit(num)
                .collect(Collectors.toList());
    }
}
