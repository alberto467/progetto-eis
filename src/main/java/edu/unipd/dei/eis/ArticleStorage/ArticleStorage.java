package edu.unipd.dei.eis.ArticleStorage;

import java.util.List;
import edu.unipd.dei.eis.Article;

/**
 * Interfaccia per la memorizzazione degli articoli
 */
public interface ArticleStorage {
    /**
     * Restituisce l'articolo con l'id specificato
     */
    public Article getArticle(String id) throws Exception;

    /**
     * Memorizza un articolo
     */
    public void storeArticle(Article article) throws Exception;

    /**
     * Restituisce tutti gli articoli memorizzati
     */
    public List<Article> getAllArticles() throws Exception;
}
