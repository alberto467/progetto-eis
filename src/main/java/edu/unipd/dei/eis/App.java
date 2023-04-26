package edu.unipd.dei.eis;

import edu.unipd.dei.eis.CLIParser.CLIParser;

/**
 * Classe principale
 */
public final class App {
    private App() {}

    /**
     * Funzione principale
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        CLIParser parser = new CLIParser(new CLICommands());

        try {
            parser.parse(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
