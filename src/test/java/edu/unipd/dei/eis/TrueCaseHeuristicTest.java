package edu.unipd.dei.eis;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;
import edu.unipd.dei.eis.TermsStore.TermsStore;

class TrueCaseHeuristicTest {
    @Test
    void testProcessCase() {
        TermsStore ts = new FileTermsStore("tmp/test");

        ts.registerArticleTerms(Stream.of("test", "Ciao", "Mario").collect(Collectors.toList()));
        ts.registerArticleTerms(Stream.of("test", "ciao", "Mario").collect(Collectors.toList()));

        new TrueCaseHeuristic(ts).processCase();

        Map<String, Integer> out = new HashMap<>();
        out.put("test", 2);
        out.put("ciao", 2);
        out.put("Mario", 2);

        assertEquals(ts.getTerms(), out);
    }
}
