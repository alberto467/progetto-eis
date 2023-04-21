package edu.unipd.dei.eis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

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

        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("GUARDIAN_API_KEY environment variable not set");

        GuardianAPI api = new GuardianAPI(apiKey);
        TermsExtractor tp = new TermsExtractor();

        try {
            Iterable<Article> articles = api.getArticles(new GuardianAPI.GetArticlesOptions()
                    .setShowFields(new String[] {"body"}).setLimit(3));

            for (Article a : articles) {
                System.out.println(a.webTitle);
                HashMap<String, Integer> terms =
                        tp.extractTerms(a.webTitle + ' ' + a.fields.body);

                ArrayList<Map.Entry<String, Integer>> termsArray =
                        new ArrayList<>(terms.entrySet());

                termsArray.sort((a1, a2) -> a2.getValue() - a1.getValue());

                for (Map.Entry<String, Integer> entry : termsArray)
                    System.out.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
