package edu.unipd.dei.eis.CLI;

import picocli.CommandLine.Option;

public class GlobalOptions {
    @Option(names = {"-v", "--verbose"})
    boolean verbose = false;

    @Option(names = {"-h", "--help"}, usageHelp = true,
        description = "Display this help and exit")
    boolean help;
}
