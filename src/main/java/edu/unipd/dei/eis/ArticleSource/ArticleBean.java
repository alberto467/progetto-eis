package edu.unipd.dei.eis.ArticleSource;

import com.opencsv.bean.CsvBindByName;
import edu.unipd.dei.eis.Article;

/**
 * Classe che rappresenta le informazioni del CSV relative ad un articolo
 */
public class ArticleBean {
    @CsvBindByName
    private String Identifier;

    // @CsvBindByName(column = "URL", required = true)
    // private String URL;

    @CsvBindByName
    private String Title;

    @CsvBindByName
    private String Body;

    // @CsvBindByName(column = "Date", required = true)
    // private Date Date;

    // @CsvBindByName(column = "Source Set", required = true)
    // private String SourceSet;

    // @CsvBindByName(column = "Source", required = true)
    // private String Source;

    Article toArticle() {
        return new Article(Identifier, Title, Body);
    }
}
