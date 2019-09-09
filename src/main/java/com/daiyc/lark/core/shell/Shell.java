package com.daiyc.lark.core.shell;

import com.daiyc.lark.core.session.Session;
import com.daiyc.lark.core.session.TtyTelnetSession;
import com.daiyc.lark.core.interaction.ReadlineInteraction;
import com.daiyc.lark.core.server.ConsoleConfig;
import io.termd.core.tty.TtyConnection;
import lombok.Data;

import java.util.function.Consumer;

/**
 * @author daiyc
 */
@Data
public class Shell implements Consumer<TtyConnection> {
    private ConsoleConfig config;

    public Shell(ConsoleConfig config) {
        this.config = config;
    }

    @Override
    public void accept(TtyConnection ttyConnection) {
        Session session = new TtyTelnetSession(ttyConnection);
        session.start(new ReadlineInteraction(session, getPrompt()));
    }

    public String getPrompt() {
        return config.getPrompt();
    }
}
