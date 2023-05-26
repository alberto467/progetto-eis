package edu.unipd.dei.eis.CLI;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "news-top-terms",
    subcommands = {
        DownloadCommand.class,
        ExtractCommand.class,
        DownloadExtractCommand.class
    })
public class RootCommand extends BaseCommand {
    public void task() throws Exception {
        CommandLine.usage(this, System.out);
    }
}
