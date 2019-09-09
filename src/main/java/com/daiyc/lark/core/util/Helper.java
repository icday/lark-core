package com.daiyc.lark.core.util;

import org.apache.commons.lang3.RegExUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author daiyc
 */
public class Helper {
    private static Pattern COMMAND_NAME_PATTERN = Pattern.compile("^\\w+");

    public static List<String> parseArgs(String line) {
        return parseArgs(line, true);
    }

    public static List<String> parseArgs(String line, boolean withCommand) {
        if (line == null || line.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> args = Stream.of(line.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        if (!withCommand && args.size() > 0) {
            args.remove(0);
        }
        return args;
    }

    public static String parseCommandName(String line) {
        List<String> args = parseArgs(line);
        if (args.isEmpty()) {
            return null;
        }

        String commandName = args.get(0);
        if (isValidCommandName(commandName)) {
            return commandName;
        } else {
            return null;
        }
    }

    public static boolean isValidCommandName(String name) {
        Matcher matcher = COMMAND_NAME_PATTERN.matcher(name);
        return matcher.matches();
    }

    public static <T> List<T> load(Class<T> type) {
        List<T> list = new ArrayList<>();
        for (T t : ServiceLoader.load(type)) {
            list.add(t);
        }
        return list;
    }

    public static String stripCommandName(String line) {
        return RegExUtils.removeFirst(line, "[^\\s]+\\s+");
    }
}
