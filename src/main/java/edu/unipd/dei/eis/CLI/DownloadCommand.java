package edu.unipd.dei.eis.CLI;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import edu.unipd.dei.eis.App;
import edu.unipd.dei.eis.DownloadManager;
import edu.unipd.dei.eis.ArticleSource.TimesCSV;
import edu.unipd.dei.eis.ArticleStorage.ArticleFileStore;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import java.io.File;

/**
 * Command to download articles from one or multiple sources.
 */
@Command(name = "download")
public class DownloadCommand extends BaseCommand {
    @Option(names = {"-s", "--sources"}, required = true,
        defaultValue = "all", description = "The sources to download from")
    private String[] sources;

    @Option(names = {"-o", "--output"}, defaultValue = "tmp/articles",
        description = "The output directory")
    private File output;

    @Option(names = {"-n", "--number"},
        description = "The number of articles to download from each source")
    private int number = 10;

    public DownloadCommand() {}

    public DownloadCommand(String[] sources, File output, int number) {
        this.sources = sources;
        this.output = output;
        this.number = number;
    }

    public void task() throws Exception {
        if (output.exists() && !output.isDirectory())
            throw new RuntimeException("Output path is not a directory");

        String apiKey = App.env.get("GUARDIAN_API_KEY");
        if (apiKey == null || apiKey.isEmpty())
            throw new RuntimeException("GUARDIAN_API_KEY environment variable not set");

        // ArticleSource source = new GuardianAPI(apiKey);
        ArticleStorage storage = new ArticleFileStore(output);

        // DownloadManager.download(new GuardianAPI(apiKey), storage, 3);
        DownloadManager.download(new TimesCSV("nytimes_articles_v2.csv"), storage, number);
    }
}
