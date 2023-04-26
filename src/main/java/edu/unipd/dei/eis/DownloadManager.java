package edu.unipd.dei.eis;

import java.util.List;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;

/**
 * Classe che si occupa di scaricare gli articoli
 */
public class DownloadManager {
    private ArticleSource source;
    private ArticleStorage storage;

    /**
     * Costruttore
     * 
     * @param source Un oggetto ArticleSource che si occupa di fornire gli articoli
     * @param storage Un oggetto ArticleStorage che si occupa di memorizzare gli articoli
     */
    public DownloadManager(ArticleSource source, ArticleStorage storage) {
        this.source = source;
        this.storage = storage;
    }

    /**
     * Scarica un numero di articoli
     * 
     * @param num Il numero di articoli da scaricare
     * 
     * @return La lista di articoli scaricati
     */
    public List<Article> download(int num) throws Exception {
        List<Article> articles = source.getArticles(num);

        for (Article a : articles)
            storage.storeArticle(a);

        return articles;
    }
}
