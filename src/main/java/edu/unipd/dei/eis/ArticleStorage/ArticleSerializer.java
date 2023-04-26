package edu.unipd.dei.eis.ArticleStorage;

import java.util.List;
import edu.unipd.dei.eis.Article;
import edu.unipd.dei.eis.JSONFileStore;

public class ArticleSerializer implements ArticleStorage {
    private final JSONFileStore<Article> store;

    public ArticleSerializer(String dir) {
        this.store = new JSONFileStore<>(dir, Article.class);
    }

    public Article getArticle(String id) throws Exception {
        return store.load(id);
    }

    public void storeArticle(Article article) throws Exception {
        store.save(article.id, article);
    }

    public List<Article> getAllArticles() throws Exception {
        return store.loadAll();
    }
}
