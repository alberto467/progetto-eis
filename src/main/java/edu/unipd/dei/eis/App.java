package edu.unipd.dei.eis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.unipd.dei.eis.CLIParser.CLIParser;

/**
 * Classe principale
 */
public final class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private App() {}

    /**
     * Funzione principale
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        CLIParser parser = new CLIParser(new CLICommands());

        try {
            parser.execute(args);
        } catch (Exception e) {
            logger.error("Error while executing command", e);
            System.exit(1);
        }
    }
}
