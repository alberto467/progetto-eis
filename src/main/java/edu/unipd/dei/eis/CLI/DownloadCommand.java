package edu.unipd.dei.eis.CLI;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import edu.unipd.dei.eis.App;
import edu.unipd.dei.eis.DownloadManager;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleStorage.ArticleFileStore;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import java.io.File;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Command to download articles from one or multiple sources.
 */
@Command(name = "download")
public class DownloadCommand extends BaseCommand {
    private static final Logger logger = LoggerFactory.getLogger(DownloadCommand.class);

    @Option(names = {"-s", "--sources"}, required = true,
        description = "The sources to download from", split = ",")
    private String[] sources = {"all"};

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

        Set<String> sourcesSet = new HashSet<String>();
        for (String source : sources)
            if (!source.isEmpty())
                sourcesSet.add(source.toLowerCase());

        boolean all = sourcesSet.size() == 0 || sourcesSet.contains("all");

        logger.info("Downloading {} articles from {}", number,
            all ? "all" : sourcesSet.toString());

        for (ArticleSource Source : ServiceLoader.load(ArticleSource.class)) {
            logger.info("Discovered source {}", Source.getName());
            if (all || sourcesSet.contains(Source.getName().toLowerCase()))
                DownloadManager.download(Source, storage, number);
        }
    }
}
