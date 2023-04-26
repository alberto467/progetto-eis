package edu.unipd.dei.eis.ArticleStorage;

import java.util.List;
import edu.unipd.dei.eis.Article;
import edu.unipd.dei.eis.JSONFileStore;

/**
 * Classe che si occupa di memorizzare gli articoli su file JSON
 */
public class ArticleSerializer implements ArticleStorage {
    private final JSONFileStore<Article> store;

    /**
     * Costruttore
     * 
     * @param dir La directory in cui memorizzare gli articoli
     */
    public ArticleSerializer(String dir) {
        this.store = new JSONFileStore<>(dir, Article.class);
    }

    /**
     * Restituisce l'articolo con l'id specificato
     */
    public Article getArticle(String id) throws Exception {
        return store.load(id);
    }

    /**
     *  Memorizza un articolo
     */
    public void storeArticle(Article article) throws Exception {
        store.save(article.id, article);
    }

    /**
     * Restituisce tutti gli articoli memorizzati
     */
    public List<Article> getAllArticles() throws Exception {
        return store.loadAll();
    }
}
