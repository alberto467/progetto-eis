package edu.unipd.dei.eis.CLI;

import java.util.ServiceLoader;
import edu.unipd.dei.eis.ArticleSource.ArticleSource;
import picocli.CommandLine.Command;

@Command(name = "list-sources", description = "List all the sources available to download")
public class ListSourcesCommand extends BaseCommand {
    public void task() throws Exception {
        System.out.println("Supported article sources:");
        for (ArticleSource Source : ServiceLoader.load(ArticleSource.class)) {
            System.out.println(" - " + Source.getName());
        }
    }


}
