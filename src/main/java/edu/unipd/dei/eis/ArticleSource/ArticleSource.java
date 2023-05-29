package edu.unipd.dei.eis.ArticleSource;

import java.util.List;
import edu.unipd.dei.eis.Article;

/**
 * Interface for article sources
 */
public interface ArticleSource {
    /**
     * Returns specified number of articles
     * 
     * @param num Number of articles to return
     * @return List of articles
     */
    public List<Article> getArticles(int num) throws Exception;

    /**
     * Returns the name of the source
     * 
     * @return Name of the source
     */
    public String getName();
}
