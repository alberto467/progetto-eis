package edu.unipd.dei.eis.CLIParser;

import java.lang.reflect.Method;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Classe che seleziona il metodo da eseguire in base agli argomenti passati da linea di comando
 */
public class CLIParser {
    private static final Logger logger = LoggerFactory.getLogger(CLIParser.class);

    BaseCLICommands commands;

    /**
     * Costruttore
     * 
     * @param commands Un oggetto che implementa l'interfaccia BaseCLICommands
     */
    public CLIParser(BaseCLICommands commands) {
        this.commands = commands;
    }

    /**
     * Esegue il metodo corrispondente agli argomenti passati da linea di comando
     * 
     * @param args Gli argomenti passati da linea di comando
     * 
     * @throws Exception
     */
    public void execute(String[] args) throws Exception {
        logger.info("Parsing args: {}" + String.join(", ", args));

        if (args.length != 1) {
            commands.help();
            throw new RuntimeException("Invalid number of arguments");
        }

        Method targetMethod;
        try {
            targetMethod = this.commands.getClass().getDeclaredMethod(args[0]);
            System.out.println("Found method: " + targetMethod.getName());
        } catch (NoSuchMethodException e) {
            commands.help();
            throw new RuntimeException("Unknown command", e);
        }

        targetMethod.invoke(this.commands);
    }
}
