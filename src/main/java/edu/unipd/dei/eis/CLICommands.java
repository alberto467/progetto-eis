package edu.unipd.dei.eis;

import edu.unipd.dei.eis.CLIParser.BaseCLICommands;
// import edu.unipd.dei.eis.ArticleSource.GuardianAPI;
import edu.unipd.dei.eis.ArticleSource.TimesCSV;
import edu.unipd.dei.eis.ArticleStorage.ArticleSerializer;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Classe che contiene i comandi da eseguire da linea di comando
 */
public class CLICommands implements BaseCLICommands {
    // private static final Logger logger = LoggerFactory.getLogger(CLICommands.class);

    /**
     * Stampa l'help
     */
    public void help() {
        System.out.println("Use one of the following commands:");
        System.out.println("  help");
        System.out.println("  download");
        System.out.println("  extract");
        System.out.println("  downloadExtract");
    }

    /**
     * Scarica gli articoli
     */
    public void download() throws Exception {
        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("GUARDIAN_API_KEY environment variable not set");

        // ArticleSource source = new GuardianAPI(apiKey);
        ArticleStorage storage = new ArticleSerializer("tmp/articles");

        // DownloadManager.download(new GuardianAPI(apiKey), storage, 3);
        DownloadManager.download(new TimesCSV("nytimes_articles_v2.csv"), storage, 1000);
    }

    /**
     * Estrae i termini
     */
    public void extract() throws Exception {
        FileTermsStore termsStore = new FileTermsStore("tmp/terms");
        ExtractionManager ap = new ExtractionManager(termsStore);
        ArticleStorage fileStorage = new ArticleSerializer("tmp/articles");

        // Iterable<Article> articles = downloadArticles();
        List<Article> articles = fileStorage.getAllArticles();
        ap.process(articles);
        termsStore.save(Timestamp.from(Instant.now()).toString());

        TopTerms topTerms = termsStore.getTopTerms(50);
        topTerms.write("tmp/top_terms.txt");
        topTerms.print();
    }

    /**
     * Scarica gli articoli e successivamente estrae i termini
     */
    public void downloadExtract() throws Exception {
        download();
        extract();
    }
}
