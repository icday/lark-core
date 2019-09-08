package com.dyc.lark.core.command;

import com.dyc.lark.core.complete.ArgCompleter;
import lombok.Data;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicReference;

import static com.dyc.lark.core.complete.ArgCompleter.NULL;

/**
 * @author daiyc
 */
@Data
public abstract class BaseCommand implements Command {
    private final String name;

    private Options options;

    private final String description;

    private AtomicReference<ArgCompleter> argCompleter = new AtomicReference<>(null);

    private static HelpFormatter formatter = new HelpFormatter();

    public BaseCommand(String name) {
        this(name, name);
    }

    public BaseCommand(String name, String description) {
        this.name = name;
        this.description = description;
        this.options = initOptions();
    }

    protected Options initOptions() {
        return new Options();
    }

    @Override
    public ArgCompleter getArgCompleter() {
        ArgCompleter completer = argCompleter.get();
        if (completer == null) {
            completer = initArgCompleter();
            argCompleter.compareAndSet(null, completer);
        }
        return completer;
    }

    protected ArgCompleter initArgCompleter() {
        return NULL;
    }

    @Override
    public String desc() {
        return String.format("%s, %s", name, description);
    }

    @Override
    public String usage() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream, true);
        formatter.printHelp(printWriter, formatter.getWidth(), name, description, options,
                formatter.getLeftPadding(), formatter.getDescPadding(), null, true);
        return outputStream.toString();
    }
}
