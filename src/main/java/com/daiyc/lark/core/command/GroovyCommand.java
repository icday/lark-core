package com.daiyc.lark.core.command;

import com.daiyc.lark.core.groovy.GroovyInteraction;

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
