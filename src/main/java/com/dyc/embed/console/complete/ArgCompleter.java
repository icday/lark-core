package com.dyc.embed.console.complete;

import java.util.Collections;
import java.util.List;

/**
 * @author daiyc
 */
public interface ArgCompleter {
    List<CharSequence> complete(List<String> args, int cursor);

    boolean partComplete();

    ArgCompleter NULL = new ArgCompleter() {
        @Override
        public List<CharSequence> complete(List<String> args, int cursor) {
            return Collections.emptyList();
        }

        @Override
        public boolean partComplete() {
            return false;
        }
    };
}
