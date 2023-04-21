package edu.unipd.dei.eis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.Annotator;

// This class implements a CoreNLP annotator
public class StopWordAnnotator implements Annotator, CoreAnnotation<Boolean> {
  public static final String ANNOTATOR_CLASS = "stopword";

  private Set<String> stopWords;

  StopWordAnnotator() throws FileNotFoundException {
    // Read from file
    File file = new File("stopwords.txt");
    Scanner reader = new Scanner(file);

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
  public Set<Class<? extends CoreAnnotation>> requirementsSatisfied() {
    return new HashSet<>(Arrays.asList(StopWordAnnotator.class));
  }

  @Override
  public Set<Class<? extends CoreAnnotation>> requires() {
    return new HashSet<>(Arrays.asList(
        CoreAnnotations.TokensAnnotation.class,
        CoreAnnotations.LemmaAnnotation.class));
  }

  @Override
  public Class<Boolean> getType() {
    return Boolean.class;
  }
}
