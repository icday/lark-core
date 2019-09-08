package com.dyc.lark.core.complete;

import com.dyc.lark.core.command.Command;
import com.dyc.lark.core.command.CommandRepository;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daiyc
 */
public class CommandCompleter implements Completer {
    private ArgumentCompleter.ArgumentDelimiter delimiter = new ArgumentCompleter.WhitespaceArgumentDelimiter();

    private Completer commandCompleter;

    private CommandRepository commandRepository;

    private final static String OPTION_CHAR = "-";

    public CommandCompleter(CommandRepository commandRepository) {
        this.commandCompleter = new StringsCompleter(commandRepository.listCommands());
        this.commandRepository = commandRepository;
    }

    /**
     *
     * @param buffer 行内容
     * @param cursor 光标所在的位置，从0开始
     * @param candidates 候选项
     * @return position: -1 表示该Completer可能无法处理该输入，继续查找Completer。这个position将作为参数传给CompletionHandler
     */
    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        ArgumentCompleter.ArgumentList argumentList = delimiter.delimit(buffer, cursor);
        int cursorArgumentIndex = argumentList.getCursorArgumentIndex();
        int argumentPosition = argumentList.getArgumentPosition();
        String[] arguments = argumentList.getArguments();

        String cursorArgument = argumentList.getCursorArgument();
        if (cursorArgumentIndex == 0) {
            return commandCompleter.complete(cursorArgument, argumentPosition, candidates);
        }
        if (!checkValid(argumentList)) {
            return -1;
        }

        String commandName = arguments[0];
        Command command = commandRepository.get(commandName);
        if (command == null) {
            return -1;
        }

        List<String> args = new ArrayList<>();

        int argIndex = filterArguments(command, argumentList, args);
        if (argIndex == -1) {
            return -1;
        }

        ArgCompleter argCompleter = command.getArgCompleter();
        if (argCompleter == null) {
            return -1;
        }
        List<CharSequence> argCandidates = argCompleter.complete(args, argIndex);

        if (argCandidates.isEmpty()) {
            return -1;
        }

        setCandidates(argCandidates, candidates, argCompleter.partialCompleter());
        if (argumentList.getCursorArgumentIndex() >= arguments.length) {
            return cursor;
        } else {
            return cursor - cursorArgument.length();
        }
    }

    private void setCandidates(List<CharSequence> candidates, List<CharSequence> output, boolean part) {
        if (!part || candidates.size() != 1) {
            output.addAll(candidates);
            return;
        }
        String str = candidates.get(0).toString() + EnhanceCompletionHandler.PART_CANDIDATE_POSTFIX;
        output.add(str);
    }

    private boolean checkValid(ArgumentCompleter.ArgumentList argumentList) {
        if (argumentList == null) {
            return false;
        }
        if (argumentList.getCursorArgumentIndex() < 0) {
            return false;
        }

        if (argumentList.getArguments().length == 0) {
            return false;
        }
        return true;
    }

    /**
     * 过滤参数，返回值为光标所在的参数序号
     *
     * @param argumentList
     * @param args
     * @return
     */
    private int filterArguments(Command command, ArgumentCompleter.ArgumentList argumentList, List<String> args) {
        // 光标所在的参数，可能大于参数数量，代表光标在行尾
        int cursorArgumentIndex = argumentList.getCursorArgumentIndex();
        // 参数列表
        String[] arguments = argumentList.getArguments();

        int argIndex = -1;
        for (int i = 1; i < arguments.length; i++) {
            String argument = arguments[i];
            if (!isOption(argument)) {
                if (cursorArgumentIndex == i) {
                    argIndex = args.size();
                }
                args.add(argument);
                continue;
            }

            if (hasValue(command, argument)) {
                i++;
            }
        }

        if (cursorArgumentIndex >= arguments.length) {
            argIndex = args.size();
        }
        return argIndex;
    }

    private boolean isOption(String optName) {
        return optName.startsWith(OPTION_CHAR);
    }

    private boolean hasValue(Command command, String optName) {
        optName = StringUtils.stripStart(optName, OPTION_CHAR);
        Options options = command.getOptions();
        Option option = options.getOption(optName);
        return !option.hasArg();
    }
}