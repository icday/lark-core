package com.dyc.embed.console.command;

import com.dyc.embed.console.groovy.GroovyInteraction;

/**
 * @author daiyc
 */
public class GroovyCommand extends PreparedCommand {
    public GroovyCommand() {
        super("groovy");
    }

    @Override
    public String doExecution(Task task, CmdContext context) {
        context.getSession().load(new GroovyInteraction(context.getSession()));
        return "";
    }
}
