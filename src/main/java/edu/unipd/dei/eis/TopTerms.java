package edu.unipd.dei.eis;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TopTerms {
    private List<Map.Entry<String, Integer>> terms;

    public TopTerms(List<Map.Entry<String, Integer>> terms) {
        this.terms = terms;
    }

    private Stream<String> getPrintStream() {
        Stream<String> out =
            terms.stream().map(e -> e.getKey().toString() + " " + e.getValue().toString());

        return out;
    }

    public void write(File path) throws Exception {
        path.mkdirs();
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

        getPrintStream().forEachOrdered(s -> {
            try {
                bw.write(s);
                bw.newLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        bw.close();
    }

    public void print() throws Exception {
        System.out.println("Top terms:");
        getPrintStream().forEachOrdered(System.out::println);
    }

    @Override
    public String toString() {
        return getPrintStream().reduce("", (a, b) -> a + b + "\n");
    }
}
