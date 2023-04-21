package edu.unipd.dei.eis;

import java.util.HashMap;
import java.util.Properties;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/*
 * Questa classe si occupa di processare il testo di un articolo per estrarre i termini e il
 * relativo numero di occorrenze.
 */
public class TermsProcessor {
  private String text;

  public TermsProcessor(String text) {
    this.text = text;
  }

  public HashMap<String, Integer> getTerms() {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, cleanxml, ssplit, pos, lemma");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

    CoreDocument document = pipeline.processToCoreDocument(text);

    HashMap<String, Integer> terms = new HashMap<String, Integer>();
    for (CoreLabel token : document.tokens()) {
      String word = token.word();

      if (terms.containsKey(word))
        terms.put(word, terms.get(word) + 1);
      else
        terms.put(word, 1);
    }
    return terms;
  }
}
