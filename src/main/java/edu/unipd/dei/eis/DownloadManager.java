package edu.unipd.dei.eis;

import java.util.List;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;

public class DownloadManager {
    private ArticleSource source;
    private ArticleStorage storage;

    public DownloadManager(ArticleSource source, ArticleStorage storage) {
        this.source = source;
        this.storage = storage;
    }

    public List<Article> download(int num) throws Exception {
        List<Article> articles = source.getArticles(num);

        for (Article a : articles)
            storage.storeArticle(a);

        return articles;
    }
}
