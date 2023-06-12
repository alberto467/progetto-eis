package edu.unipd.dei.eis.ArticleSource;

import java.util.List;
import edu.unipd.dei.eis.App;
import edu.unipd.dei.eis.Article;

public class GuardianSource implements ArticleSource {
    private GuardianAPI api = null;

    private void setupGuardianAPI() {
        String apiKey = App.env.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException(
                "Variabile d'ambiente GUARDIAN_API_KEY non impostata. Specificarla nel file .env per poter usare GuardianSource");

        api = new GuardianAPI(apiKey);
    }

    /**
     * Ritorna il nome della fonte
     * 
     * @return Il nome della fonte
     */
    public String getName() {
        return "GuardianAPI";
    }

    /**
     * Ottiene un numero di articoli usando opzioni di default e gestendo la paginazione
     * 
     * @param num Il numero di articoli da ottenere
     * 
     * @return La lista di articoli
     */
    public List<Article> getArticles(int num) throws Exception {
        // Lazily initialize the API to avoid env key errors if the user doesn't utilize this source
        if (api == null)
            setupGuardianAPI();

        return api.search(
            new GuardianAPI.SearchQueryBuilder()
                .setQuery("nuclear power")
                .setShowFields("bodyText")
                .setLimit(num)
                .build());
    }

}
