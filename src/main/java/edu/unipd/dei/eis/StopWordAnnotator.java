package edu.unipd.dei.eis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;
import edu.stanford.nlp.util.ArraySet;

/**
 * A custom CoreNLP annotator to mark tokens as stop words. It reads the stop words from a file and
 * matches them against the lemma of the token.
 * 
 * @see <a href="https://stanfordnlp.github.io/CoreNLP/new_annotator.html">CoreNLP Custom
 *      Annotators</a>
 */
public class StopWordAnnotator implements Annotator, CoreAnnotation<Boolean> {
    public static final String ANNOTATOR_CLASS = "stopword";

    private Set<String> stopWords;

    StopWordAnnotator(String name, Properties props) throws FileNotFoundException {
        String filename = props.getProperty("stopword.file");

        parseFile(filename);
    }

    private void parseFile(String filename) throws FileNotFoundException {
        Scanner reader;

        // Read from file if it is specified, otherwise read from the default file in the resources
        if (filename != null && !filename.isEmpty()) {
            File file = new File(filename);
            reader = new Scanner(file);
        }
        else {
            InputStream is = getClass().getResourceAsStream("/stopwords.txt");
            reader = new Scanner(is);
        }

        stopWords = new HashSet<>();
        while (reader.hasNextLine())
            stopWords.add(reader.nextLine().trim().toLowerCase());

        reader.close();
    }

    @Override
    public void annotate(Annotation annotation) {
        if (annotation.containsKey(CoreAnnotations.TokensAnnotation.class))
            for (CoreLabel token : annotation.get(CoreAnnotations.TokensAnnotation.class))
                token.set(StopWordAnnotator.class, stopWords.contains(token.lemma().toLowerCase()));
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
        return Collections.emptySet();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Set<Class<? extends CoreAnnotation>> requires() {
        return Collections.unmodifiableSet(new ArraySet<>(Arrays.asList(
            CoreAnnotations.TokensAnnotation.class, CoreAnnotations.LemmaAnnotation.class)));
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }
}
