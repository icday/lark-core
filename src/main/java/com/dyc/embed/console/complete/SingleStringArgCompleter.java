package com.dyc.embed.console.complete;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author daiyc
 */
public class SingleStringArgCompleter implements ArgCompleter {
    private List<String> candidates;

    private Supplier<List<String>> supplier;

    private boolean partComplete = false;

    public SingleStringArgCompleter(Supplier<List<String>> supplier) {
        this(supplier, false);
    }

    public SingleStringArgCompleter(List<String> candidates) {
        this(candidates, false);
    }

    public SingleStringArgCompleter(List<String> candidates, boolean partComplete) {
        this.candidates = candidates;
        this.partComplete = partComplete;
    }

    public SingleStringArgCompleter(Supplier<List<String>> supplier, boolean partComplete) {
        this.supplier = supplier;
        this.partComplete = partComplete;
    }

    @Override
    public boolean partComplete() {
        return partComplete;
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

        final String prefix = arg;
        List<String> candidates;
        if (supplier == null) {
            candidates = this.candidates;
        } else {
            candidates = supplier.get();
        }

        return candidates.stream()
                .filter(candidate -> StringUtils.isBlank(prefix) || candidate.startsWith(prefix))
                .map(s -> (CharSequence) s)
                .collect(Collectors.toList());
    }
}
