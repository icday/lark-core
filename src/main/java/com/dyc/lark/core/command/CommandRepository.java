package com.dyc.lark.core.command;

import com.dyc.lark.core.util.Helper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daiyc
 */
@NoArgsConstructor
public class CommandRepository {
    @Getter
    private List<Command> commands = new ArrayList<>();

    private Map<String, Command> commandMap = new HashMap<>();

    private CommandRepository(List<Command> commands) {
        this.registry(commands);
    }

    public static CommandRepository createDefaultRepository() {
        List<Command> commands = Helper.load(Command.class);
        return new CommandRepository(commands);
    }

    public synchronized List<Command> find(String s) {
        if (s == null || s.isEmpty()) {
            return commands;
        }

        return commands.stream().filter(c -> c.getName().equals(s) || c.getName().startsWith(s)).collect(Collectors.toList());
    }

    public synchronized CommandRepository registry(Command command) {
        commands.add(command);
        commandMap.put(command.getName(), command);
        return this;
    }

    public synchronized CommandRepository registry(Command... commands) {
        return registry(Arrays.asList(commands));
    }

    public synchronized CommandRepository registry(Collection<Command> commands) {
        for (Command command : commands) {
            registry(command);
            if (command instanceof CommandRepositoryAware) {
                ((CommandRepositoryAware) command).setCommandRepository(this);
            }
        }
        return this;
    }

    public synchronized Command get(String commandName) {
        return commandMap.get(commandName);
    }

    public List<String> listCommands() {
        return commands.stream().map(Command::getName).collect(Collectors.toList());
    }
}
