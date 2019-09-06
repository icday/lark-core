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

    private DefaultSingleArgCompleter(boolean partialCompleter) {
        super(partialCompleter);
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
        if (candidates == null) {
            candidates = Collections.emptyList();
        }

        return candidates.stream()
                .filter(candidate -> StringUtils.isBlank(prefix) || candidate.startsWith(prefix))
                .map(s -> (CharSequence) s)
                .collect(Collectors.toList());
    }

    public static class Builder {
        private Supplier<List<String>> supplier;

        private Function<String, List<String>> function;

        private boolean partial = false;

        private Builder() {
        }

        public Builder datasource(Function<String, List<String>> supplier) {
            this.function = supplier;
            return this;
        }

        public Builder datasource(Supplier<List<String>> supplier) {
            this.supplier = supplier;
            return this;
        }

        public Builder datasource(List<String> candidates) {
            this.supplier = () -> candidates;
            return this;
        }

        public Builder partial(boolean partial) {
            this.partial = partial;
            return this;
        }

        public DefaultSingleArgCompleter build() {
            DefaultSingleArgCompleter completer = new DefaultSingleArgCompleter(partial);
            if (function == null && supplier == null) {
                throw new IllegalArgumentException();
            }
            completer.completeFunction = function;
            completer.supplier = supplier;
            return completer;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
