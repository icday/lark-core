package com.dyc.embed.console.command;

import com.dyc.embed.console.session.Session;
import com.dyc.embed.console.util.Helper;

/**
 * @author daiyc
 */
public class CommandFacade {
    private CommandRepository commandRepository;

    public CommandFacade(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public String execute(String line, Session session) {
        Command command;
        String commandName = Helper.parseCommandName(line);
        command = commandRepository.get(commandName);
        if (command == null) {
            throw new RuntimeException(String.format("command not found: %s", commandName));
        }
        return command.execute(line, createNewContext(session));
    }

    private CmdContext createNewContext(Session session) {
        return new CmdContext()
                .setCommandRepository(commandRepository)
                .setSession(session);
    }
}
