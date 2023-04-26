package edu.unipd.dei.eis;

import edu.unipd.dei.eis.CLIParser.BaseCLICommands;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleSource.GuardianAPI;
import edu.unipd.dei.eis.ArticleStorage.ArticleSerializer;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Map;
import edu.unipd.dei.eis.TermsStore.FileTermsStore;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Classe che contiene i comandi da eseguire da linea di comando
 */
public class CLICommands implements BaseCLICommands {
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
        System.out.println("Downloading...");

        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("GUARDIAN_API_KEY environment variable not set");

        ArticleSource source = new GuardianAPI(apiKey);
        ArticleStorage storage = new ArticleSerializer("tmp/articles");

        DownloadManager dm = new DownloadManager(source, storage);

        dm.download(3);

        System.out.println("Download complete!");
    }

    /**
     * Estrae i termini
     */
    public void extract() throws Exception {
        System.out.println("Extracting...");
        FileTermsStore termsStore = new FileTermsStore("tmp/terms");
        ArticleProcessor ap = new ArticleProcessor(termsStore);
        ArticleStorage fileStorage = new ArticleSerializer("tmp/articles");

        // Iterable<Article> articles = downloadArticles();
        Iterable<Article> articles = fileStorage.getAllArticles();
        ap.process(articles);
        termsStore.save(Timestamp.from(Instant.now()).toString());

        for (Map.Entry<String, Integer> entry : termsStore.getTopTerms(50))
            System.out.println(entry.getKey() + " " + entry.getValue());

        System.out.println("Extraction complete!");
    }

    /**
     * Scarica gli articoli e successivamente estrae i termini
     */
    public void downloadExtract() throws Exception {
        download();
        extract();
    }
}
