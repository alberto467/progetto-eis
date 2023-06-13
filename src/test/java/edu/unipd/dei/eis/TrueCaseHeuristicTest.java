package edu.unipd.dei.eis;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrueCaseHeuristicTest {
    @Test
    void testProcessCase() {
        List<Set<String>> termsSets = Arrays.asList(
            new HashSet<>(Arrays.asList("test", "Ciao", "Mario")),
            new HashSet<>(Arrays.asList("test", "ciao", "Mario")));

        TrueCaseHeuristic.processCase(termsSets);

        List<Set<String>> expected = Arrays.asList(
            new HashSet<>(Arrays.asList("test", "ciao", "Mario")),
            new HashSet<>(Arrays.asList("test", "ciao", "Mario")));

        assertEquals(expected, termsSets);
    }
}
