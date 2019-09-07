package com.dyc.embed.console.interaction;

import com.dyc.embed.console.command.CommandFacade;
import com.dyc.embed.console.command.CommandRepository;
import com.dyc.embed.console.complete.CommandCompleter;
import com.dyc.embed.console.complete.EnhanceCompletionHandler;
import com.dyc.embed.console.session.Session;
import io.termd.core.util.Vector;
import jline.UnixTerminal;
import jline.console.ConsoleReader;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiRenderWriter;

import java.io.IOException;
import java.io.PrintWriter;

import static com.dyc.embed.console.interaction.RetCode.CONTINUE;
import static com.dyc.embed.console.interaction.RetCode.EXIT;

/**
 * @author daiyc
 */
public class ReadlineInteraction extends BaseInteraction {
    private class TelnetTerminal extends UnixTerminal {
        private Vector defaultSize = new Vector(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        public TelnetTerminal() throws Exception {
            super();
        }

        @Override
        public int getWidth() {
            return getSize().x();
        }

        @Override
        public int getHeight() {
            return getSize().y();
        }

        private Vector getSize() {
            Vector size = session.size();
            return size == null ? defaultSize : size;
        }
    }

    private final ConsoleReader consoleReader;
    private final PrintWriter writer;
    private CommandRepository commandRepository;

    private CommandFacade commandFacade;

    public ReadlineInteraction(Session session, String prompt) {
        super(session, prompt);

        writer = new AnsiRenderWriter(session.outputStream(), true);

        commandRepository = CommandRepository.createDefaultRepository();
        commandFacade = new CommandFacade(commandRepository);
        try {
            consoleReader = createConsole();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private ConsoleReader createConsole() throws Exception {
        TelnetTerminal telnetTerminal = new TelnetTerminal();
        ConsoleReader consoleReader = new ConsoleReader(session.inputStream(), session.outputStream(), telnetTerminal);
        consoleReader.addCompleter(new CommandCompleter(commandRepository));
        consoleReader.setCompletionHandler(new EnhanceCompletionHandler());
        return consoleReader;
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public RetCode loop() {
        try {
            String s = consoleReader.readLine(prompt);
            if (s == null) {
                return EXIT;
            }
            if (StringUtils.isBlank(s)) {
                return CONTINUE;
            }

            processLine(s);
        } catch (IOException e) {
            e.printStackTrace(writer);
            return EXIT;
        }
        return CONTINUE;
    }

    private void processLine(String line) {
        try {
            String result = commandFacade.execute(line, session);
            if (session.interaction() != this) {
                return;
            }
            if (result != null) {
                session.send(result);
                session.send("\n");
            }
        } catch (Throwable ex) {
            ex.printStackTrace(writer);
            session.send(String.format("%s: %s\n", ex.getClass().getName(), ex.getMessage()));
        }
    }
}
