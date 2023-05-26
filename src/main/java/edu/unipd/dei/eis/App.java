package edu.unipd.dei.eis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import edu.unipd.dei.eis.CLI.RootCommand;
import io.github.cdimascio.dotenv.Dotenv;
import picocli.CommandLine;

/**
 * Classe principale
 */
public final class App {
    private static final Logger logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    public static final Dotenv env = Dotenv.load();

    private App() {}

    /**
     * Funzione principale
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        CommandLine cl = new CommandLine(new RootCommand());

        System.exit(cl.execute(args));
    }

    /**
     * Permette di cambiare il livello di log dinamicamente
     * 
     * @param level Livello di log di tipo {@link Level}
     */
    public static void setLogLevel(Level level) {
        if (logger instanceof ch.qos.logback.classic.Logger) {
            ch.qos.logback.classic.Logger lb = (ch.qos.logback.classic.Logger) logger;
            lb.setLevel(level);
        }
    }
}
