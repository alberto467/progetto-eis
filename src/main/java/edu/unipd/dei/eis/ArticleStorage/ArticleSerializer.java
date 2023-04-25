package edu.unipd.dei.eis.ArticleStorage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import edu.unipd.dei.eis.Article;

public class ArticleSerializer implements ArticleStorage {
    private Path dir;
    private static final Gson g = new Gson();
    private static final Slugify slug = new Slugify();

    public ArticleSerializer(String dir) {
        this.dir = Paths.get(System.getProperty("user.dir"), dir);
        this.dir.toFile().mkdirs();
    }

    private Path getFilePath(String id) {
        String slugId = slug.slugify(id);
        return dir.resolve(slugId + ".json");
    }

    public Article getArticle(String id)
            throws FileNotFoundException, IOException, JsonIOException, JsonSyntaxException {
        File file = getFilePath(id).toFile();

        BufferedReader r = new BufferedReader(new FileReader(file));
        Article article = g.fromJson(r, Article.class);
        r.close();

        return article;
    }

    public void storeArticle(Article article) throws IOException {
        File file = getFilePath(article.id).toFile();

        if (file.exists()) {
            System.out.println("File " + file + " already exists, skipping");
            return;
        }

        FileWriter fw = new FileWriter(file);
        g.toJson(article, Article.class, fw);
        fw.close();
    }

    public List<Article> getAllArticles() throws Exception {
        return Arrays.stream(dir.toFile().listFiles()).map(file -> file.getName())
                .filter(fileName -> fileName.endsWith(".json"))
                .map(fileName -> fileName.substring(0, fileName.length() - 5)).map(id -> {
                    try {
                        return getArticle(id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }
}
