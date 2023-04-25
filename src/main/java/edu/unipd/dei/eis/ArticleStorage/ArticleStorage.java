package edu.unipd.dei.eis.ArticleStorage;

import java.util.List;
import edu.unipd.dei.eis.Article;

public interface ArticleStorage {
    public Article getArticle(String id) throws Exception;

    public void storeArticle(Article article) throws Exception;

    public List<Article> getAllArticles() throws Exception;
}
