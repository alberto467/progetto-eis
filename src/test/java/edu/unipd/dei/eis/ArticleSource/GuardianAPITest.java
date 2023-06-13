package edu.unipd.dei.eis.ArticleSource;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import edu.unipd.dei.eis.Article;
import io.github.cdimascio.dotenv.Dotenv;

class GuardianAPITest {
    @Test
    void testGetArticles() {
        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("GUARDIAN_API_KEY");
        if (apiKey == null)
            apiKey = System.getenv("GUARDIAN_API_KEY");

        GuardianAPI gApi = new GuardianAPI(apiKey);

        assertDoesNotThrow(() -> {
            List<Article> articles = gApi.search(
                new GuardianAPI.SearchQueryBuilder()
                    .setQuery("cats")
                    .setShowFields("bodyText")
                    .setLimit(3)
                    .build());

            assertEquals(3, articles.size());
            assertTrue(articles.get(0).id.length() > 0);
            assertTrue(articles.get(0).title.length() > 0);
            assertTrue(articles.get(0).body.length() > 0);
        });
    }
}
