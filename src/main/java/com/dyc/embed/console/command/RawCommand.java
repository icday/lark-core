package com.dyc.embed.console.command;

import com.dyc.embed.console.util.Helper;

/**
 * @author daiyc
 */
public abstract class RawCommand extends BaseCommand {
    public RawCommand(String name) {
        super(name);
    }

    public RawCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public String execute(String line, CmdContext context) {
        String argStr = Helper.stripCommandName(line);
        return doExecution(argStr, context);
    }

    protected abstract String doExecution(String argStr, CmdContext context);
}
