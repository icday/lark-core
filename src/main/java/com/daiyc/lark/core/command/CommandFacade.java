package com.daiyc.lark.core.command;

import com.daiyc.lark.core.session.Session;
import com.daiyc.lark.core.util.Helper;

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
