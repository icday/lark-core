package com.dyc.embed.console.command;

import com.dyc.embed.console.util.Helper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.List;

/**
 * @author daiyc
 */
public abstract class PreparedCommand extends BaseCommand {

    public PreparedCommand(String name) {
        super(name);
    }

    public PreparedCommand(String name, String description) {
        super(name, description);
    }

    private static final CommandLineParser PARSER = new DefaultParser();

    @Override
    public String execute(String line, CmdContext context) {
        List<String> args = Helper.parseArgs(line);
        if (args.size() == 0) {
            return null;
        }

        String commandName = args.get(0);
        Command command = context.getCommandRepository().get(commandName);
        if (command == null) {
            throw new RuntimeException(String.format("command not found: %s", commandName));
        }

        try {
            String[] argArr = new String[args.size() - 1];
            args.subList(1, args.size()).toArray(argArr);
            CommandLine commandLine = PARSER.parse(command.getOptions(), argArr);
            Task task = new Task(commandLine);
            return doExecution(task, context);
        } catch (org.apache.commons.cli.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String doExecution(Task task, CmdContext context);
}
