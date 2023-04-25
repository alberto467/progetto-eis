package edu.unipd.dei.eis;

import java.util.List;
import java.util.Map;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleSource.GuardianAPI;
import edu.unipd.dei.eis.ArticleStorage.ArticleSerializer;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import edu.unipd.dei.eis.TermsStore.MemoryTermsStore;
import edu.unipd.dei.eis.TermsStore.Utils;
import io.github.cdimascio.dotenv.Dotenv;

// Scrivere serializzazione da oggetto a file

public final class App {
    private App() {}

    private static List<Article> downloadArticles() throws Exception {
        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("GUARDIAN_API_KEY environment variable not set");

        ArticleSource source = new GuardianAPI(apiKey);
        ArticleStorage storage = new ArticleSerializer("tmp/articles");

        DownloadManager dm = new DownloadManager(source, storage);

        return dm.download(3);
    }

    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");

        ArticleProcessor ap = new ArticleProcessor(new MemoryTermsStore());
        ArticleStorage fileStorage = new ArticleSerializer("tmp/articles");

        try {
            // Iterable<Article> articles = downloadArticles();
            Iterable<Article> articles = fileStorage.getAllArticles();
            ap.process(articles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : Utils.getTopTerms(ap.getTerms(), 50))
            System.out.println(entry.getKey() + " " + entry.getValue());
    }
}
