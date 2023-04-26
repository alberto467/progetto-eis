package edu.unipd.dei.eis.CLIParser;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CLIParser {
    BaseCLICommands commands;

    public CLIParser(BaseCLICommands commands) {
        this.commands = commands;
    }

    public void parse(String[] args) throws Exception {
        String joinedArgs = String.join(", ", args);
        System.out.println("Parsing args: " + joinedArgs);

        if (args.length != 1) {
            commands.help();
            return;
        }

        Method targetMethod;
        try {
            targetMethod = this.commands.getClass().getDeclaredMethod(args[0]);
            System.out.println("Found method: " + targetMethod.getName());
        } catch (NoSuchMethodException e) {
            System.out.println("Unknown command");
            commands.help();
            return;
        }
        if (!Modifier.isPublic(targetMethod.getModifiers())) {
            System.out.println("Unknown command");
            commands.help();
            return;
        }

        targetMethod.invoke(this.commands);
    }
}
