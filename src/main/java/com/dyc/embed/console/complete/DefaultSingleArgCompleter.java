package com.dyc.embed.console.complete;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author daiyc
 */
public class DefaultSingleArgCompleter extends AbstractSingleArgCompleter {
    private Supplier<List<String>> supplier;

    public DefaultSingleArgCompleter(List<String> candidates) {
        this(() -> candidates);
    }

    public DefaultSingleArgCompleter(boolean partialCompleter, List<String> candidates) {
        this(partialCompleter, () -> candidates);
    }

    public DefaultSingleArgCompleter(Supplier<List<String>> supplier) {
        this.supplier = supplier;
    }

    public DefaultSingleArgCompleter(boolean partialCompleter, Supplier<List<String>> supplier) {
        super(partialCompleter);
        this.supplier = supplier;
    }

    @Override
    protected List<CharSequence> doComplete(String arg) {
        final String prefix = arg;
        if (supplier == null) {
            return Collections.emptyList();
        }

        List<String> candidates = supplier.get();
        return candidates.stream()
                .filter(candidate -> StringUtils.isBlank(prefix) || candidate.startsWith(prefix))
                .map(s -> (CharSequence) s)
                .collect(Collectors.toList());
    }
}
