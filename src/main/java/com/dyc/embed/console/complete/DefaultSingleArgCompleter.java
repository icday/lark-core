package com.dyc.embed.console.complete;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author daiyc
 */
public class DefaultSingleArgCompleter extends AbstractSingleArgCompleter {
    private Supplier<List<String>> supplier;

    private Function<String, List<String>> completeFunction;

    public DefaultSingleArgCompleter(Function<String, List<String>> completeFunction) {
        this.completeFunction = completeFunction;
    }

    public DefaultSingleArgCompleter(boolean partialCompleter, Function<String, List<String>> completeFunction) {
        super(partialCompleter);
        this.completeFunction = completeFunction;
    }

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
        if (supplier == null && completeFunction == null) {
            return Collections.emptyList();
        }
        List<String> candidates;
        if (completeFunction != null) {
            candidates = completeFunction.apply(arg);
        } else {
            candidates = supplier.get();
        }

        return candidates.stream()
                .filter(candidate -> StringUtils.isBlank(prefix) || candidate.startsWith(prefix))
                .map(s -> (CharSequence) s)
                .collect(Collectors.toList());
    }
}
