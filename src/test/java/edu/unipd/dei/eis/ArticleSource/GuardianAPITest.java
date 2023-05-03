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

        GuardianAPI gApi = new GuardianAPI(apiKey);

        assertDoesNotThrow(() -> {
            List<Article> articles = gApi.getArticles(3);

            assertEquals(articles.size(), 3);
            assertTrue(articles.get(0).id.length() > 0);
            assertTrue(articles.get(0).title.length() > 0);
            assertTrue(articles.get(0).body.length() > 0);
        });
    }
}
