package com.dyc.embed.console.command;

import com.dyc.embed.console.complete.ArgCompleter;
import com.dyc.embed.console.complete.SingleStringArgCompleter;
import org.apache.commons.cli.CommandLine;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author daiyc
 */
public class HelpCommand extends PreparedCommand implements CommandRepositoryAware {
    public HelpCommand() {
        super("help", "Print help info");
    }

    private CommandRepository commandRepository;

    @Override
    public void setCommandRepository(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    @Override
    protected ArgCompleter initArgCompleter() {
        return new SingleStringArgCompleter(commandRepository.listCommands());
    }

    @Override
    public String doExecution(Task task, CmdContext context) {
        CommandLine commandLine = task.getCommandLine();

        CommandRepository commandRepository = context.getCommandRepository();

        if (commandLine.getArgList().isEmpty()) {
            List<Command> commands = commandRepository.getCommands();
            return String.join("\n", commands.stream().map(Command::desc).collect(Collectors.toList()));
        } else {
            String cmd = commandLine.getArgList().get(0);
            Command command = commandRepository.get(cmd);
            if (command == null) {
                throw new RuntimeException(String.format("Command %s is not exists", cmd));
            }
            return command.usage();
        }
    }
}
