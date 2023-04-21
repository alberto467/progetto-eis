package edu.unipd.dei.eis;

import java.util.HashMap;

// Scrivere serializzazione da oggetto a file

public final class App {
    private App() {}

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
        GuardianAPI api = new GuardianAPI("8c784bda-11d9-4cb7-97d0-b51124d9016d");;
        try {
            Iterable<Article> articles = api.getArticles(new GuardianAPI.GetArticlesOptions()
                    .setShowFields(new String[] {"body"})
                    .setLimit(1));

            for (Article a : articles) {
                System.out.println(a.webTitle);
                System.out.println(a.fields.body);
                TermsProcessor tp = new TermsProcessor(a.fields.body);
                HashMap<String, Integer> terms = tp.getTerms();
                // terms.entrySet()
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
