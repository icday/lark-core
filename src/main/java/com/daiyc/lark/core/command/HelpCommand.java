package com.daiyc.lark.core.command;

import com.daiyc.lark.core.complete.ArgCompleter;
import com.daiyc.lark.core.complete.DefaultSingleArgCompleter;
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
        return DefaultSingleArgCompleter
                .builder()
                .datasource(commandRepository.listCommands())
                .build();
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
