package edu.unipd.dei.eis.CLI;

import java.util.concurrent.Callable;
import edu.unipd.dei.eis.App;
import picocli.CommandLine;
import picocli.CommandLine.Mixin;

/**
 * Classe da cui ereditano tutti i comandi, gestisce le opzioni globali
 */
public abstract class BaseCommand implements Callable<Void> {
    /**
     * Metodo che implementa il task del comando
     */
    public abstract void task() throws Exception;

    @Mixin
    private GlobalOptions globalOptions = new GlobalOptions();

    @Override
    public Void call() throws Exception {
        if (globalOptions.verbose)
            App.setLogLevel(ch.qos.logback.classic.Level.TRACE);

        if (globalOptions.help) {
            CommandLine.usage(this, System.out);
            return null;
        }

        task();

        return null;
    }
}
