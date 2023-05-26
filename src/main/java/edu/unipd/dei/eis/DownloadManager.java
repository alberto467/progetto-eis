package edu.unipd.dei.eis;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;

/**
 * Classe che si occupa di scaricare gli articoli
 */
public class DownloadManager {
    private static final Logger logger = LoggerFactory.getLogger(DownloadManager.class);

    /**
     * Scarica un numero di articoli
     * 
     * @param source
     *        Un oggetto ArticleSource che si occupa di fornire gli articoli
     * @param storage
     *        Un oggetto ArticleStorage che si occupa di memorizzare gli articoli
     * @param num
     *        Il numero di articoli da scaricare
     * 
     * @return La lista di articoli scaricati
     */
    public static List<Article> download(ArticleSource source, ArticleStorage storage, int num)
        throws Exception {
        logger.info("Downloading from {}...", source.getClass().getSimpleName());

        List<Article> articles = source.getArticles(num);

        for (Article a : articles)
            storage.storeArticle(a);

        logger.info("Downloaded {} articles from {}", articles.size(),
            source.getClass().getSimpleName());

        return articles;
    }
}
