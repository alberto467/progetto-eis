package edu.unipd.dei.eis.ArticleSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private String path;

    /**
     * Costruttore
     * 
     * Utilizza il file nytimes_articles_v2.csv
     */
    public TimesCSVSource() {
        this("nytimes_articles_v2.csv");
    }

    /**
     * Costruttore
     * 
     * @param path Il percorso del file CSV
     */
    public TimesCSVSource(String path) {
        this.path = path;
    }

    /**
     * Get specified number of articles (limited to number of articles in csv file)
     * 
     * @param num Number of articles to return
     * @return List of articles
     */
    public List<Article> getArticles(int num) throws Exception {
        FileReader fr = null;
        try {
            fr = new FileReader(this.path);
        } catch (FileNotFoundException e) {
            logger.info("File not found", e);
            throw e;
        }

        CsvToBean<ArticleBean> reader =
            new CsvToBeanBuilder<ArticleBean>(fr).withType(ArticleBean.class).build();

        return reader.stream().limit(num).map(ArticleBean::toArticle).collect(Collectors.toList());
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
