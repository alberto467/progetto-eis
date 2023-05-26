package edu.unipd.dei.eis.ArticleStorage;

import java.io.File;
import java.util.List;
import edu.unipd.dei.eis.Article;
import edu.unipd.dei.eis.JSONFileStore;

/**
 * Classe che si occupa di memorizzare gli articoli su file JSON
 */
public class ArticleFileStore implements ArticleStorage {
    private final JSONFileStore<Article> store;

    /**
     * Costruttore
     * 
     * @param dir La directory in cui memorizzare gli articoli
     */
    public ArticleFileStore(File dir) {
        this.store = new JSONFileStore<>(dir, Article.class);
    }

    /**
     * Restituisce l'articolo con l'id specificato
     */
    public Article getArticle(String id) throws Exception {
        return store.load(id);
    }

    /**
     * Memorizza un articolo
     */
    public void storeArticle(Article article) throws Exception {
        store.save(article.id, article);
    }

    /**
     * Esiste un articolo con l'id specificato?
     */
    public boolean hasArticle(String id) throws Exception {
        return store.has(id);
    }

    /**
     * Restituisce tutti gli articoli memorizzati
     */
    public List<Article> getAllArticles() throws Exception {
        return store.loadAll();
    }
}
