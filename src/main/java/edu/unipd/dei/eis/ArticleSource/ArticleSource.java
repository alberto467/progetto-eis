package edu.unipd.dei.eis.ArticleSource;

import java.util.List;
import edu.unipd.dei.eis.Article;

public interface ArticleSource {
    public List<Article> getArticles(int num) throws Exception;
}
