package com.dyc.embed.console.complete;

import java.util.Collections;
import java.util.List;

/**
 * @author daiyc
 */
public abstract class AbstractSingleArgCompleter implements ArgCompleter {
    private boolean partialCompleter = false;

    public AbstractSingleArgCompleter() {
        this(false);
    }

    public AbstractSingleArgCompleter(boolean partialCompleter) {
        this.partialCompleter = partialCompleter;
    }

    @Override
    public boolean partialCompleter() {
        return partialCompleter;
    }

    @Override
    public List<CharSequence> complete(List<String> args, int cursor) {
        if (args.size() > 1 || (args.size() == 1 && cursor >= 1)) {
            return Collections.emptyList();
        }
        String arg = null;
        if (!args.isEmpty()) {
            arg = args.get(cursor);
        }
        return doComplete(arg);
    }

    protected abstract List<CharSequence> doComplete(String arg);
}
