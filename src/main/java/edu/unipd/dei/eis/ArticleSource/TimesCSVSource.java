package edu.unipd.dei.eis.ArticleSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import edu.unipd.dei.eis.Article;

/**
 * Classe che si occupa di fornire gli articoli tramite un file CSV
 */
public class TimesCSVSource implements ArticleSource {
    private static final Logger logger = LoggerFactory.getLogger(TimesCSVSource.class);

    /**
     * Costruttore di default
     * 
     * Utilizza il file nytimes_articles_v2.csv nelle risorse
     */
    public TimesCSVSource() {}

    /**
     * Get specified number of articles (limited to number of articles in csv file)
     * 
     * @param num Number of articles to return
     * @return List of articles
     */
    public List<Article> getArticles(int num) throws Exception {
        InputStream is = getClass().getResourceAsStream("/nytimes_articles_v2.csv");
        if (is == null) {
            logger.error("Resource nytimes_articles_v2.csv missing!");
            throw new RuntimeException("Resource not found!");
        }
        InputStreamReader fr = new InputStreamReader(is);

        CsvToBean<ArticleBean> reader =
            new CsvToBeanBuilder<ArticleBean>(fr).withType(ArticleBean.class).build();

        List<Article> articles =
            reader.stream().limit(num).map(ArticleBean::toArticle).collect(Collectors.toList());

        if (articles.size() < num) {
            logger.warn("Requested {} articles, but only {} available", num, articles.size());
        }

        return articles;
    }

    /**
     * Returns the name of the source
     * 
     * @return Name of the source
     */
    public String getName() {
        return "TimesCSV";
    }
}
