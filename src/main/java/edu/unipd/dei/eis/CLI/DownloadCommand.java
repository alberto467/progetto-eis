package edu.unipd.dei.eis.CLI;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import edu.unipd.dei.eis.App;
import edu.unipd.dei.eis.DownloadManager;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import edu.unipd.dei.eis.ArticleStorage.ArticleFileStore;
import edu.unipd.dei.eis.ArticleStorage.ArticleStorage;
import java.io.File;
import java.util.Arrays;
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

    @Option(names = {"-s", "--sources"},
        description = "A list of sources to download from, separated by a comma. All will use all the available sources (the default). List supported sources with the list-sources command",
        split = ",")
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


        ServiceLoader<ArticleSource> sourcesLoader = ServiceLoader.load(ArticleSource.class);
        Set<String> supportedSourcesNames = new HashSet<String>();
        for (ArticleSource source : sourcesLoader) {
            logger.info("Discovered source {}", source.getName());
            supportedSourcesNames.add(source.getName().toLowerCase());
        }
        if (supportedSourcesNames.isEmpty())
            throw new RuntimeException("No sources discovered!");

        Set<String> targetSources = new HashSet<String>();
        for (String source : sources) {
            String lcSource = source.toLowerCase();
            if (source == "all" || supportedSourcesNames.contains(lcSource))
                targetSources.add(lcSource);
        }

        boolean all = targetSources.contains("all");
        if (!all && targetSources.isEmpty())
            throw new RuntimeException(
                "No valid sources specified with " + Arrays.toString(sources)
                    + "! Use the list-sources subcommand to see the list of available sources");

        logger.info("Downloading {} articles from {}", number,
            all ? "all" : targetSources.toString());

        for (ArticleSource source : sourcesLoader) {
            if (all || targetSources.contains(source.getName().toLowerCase()))
                DownloadManager.download(source, storage, number);
        }
    }
}
