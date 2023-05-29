package edu.unipd.dei.eis;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;
import edu.unipd.dei.eis.TermsStore.TermsStore;

class TrueCaseHeuristicTest {
    @Test
    void testProcessCase() {
        TermsStore ts = new FileTermsStore(new File("tmp/test"));

        ts.registerArticleTerms(Arrays.asList("test", "Ciao", "Mario"));
        ts.registerArticleTerms(Arrays.asList("test", "ciao", "Mario"));

        new TrueCaseHeuristic(ts.getTerms()).processCase();

        Map<String, Integer> out = new HashMap<>();
        out.put("test", 2);
        out.put("ciao", 2);
        out.put("Mario", 2);

        assertEquals(out, ts.getTerms());
    }
}
