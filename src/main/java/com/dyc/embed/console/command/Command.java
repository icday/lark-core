package com.dyc.embed.console.command;

import com.dyc.embed.console.complete.ArgCompleter;
import org.apache.commons.cli.Options;

/**
 * @author daiyc
 */
public interface Command {
    /**
     * 命令名
     *
     * @return name
     */
    String getName();

    /**
     * 处理一行输入，把返回值作为输出内容返回（抛出的异常也会作为显示内容）
     * @param line input line
     * @param context context
     * @return output content
     */
    String execute(String line, CmdContext context);

    /**
     * 简要描述
     *
     * @return desc
     */
    String desc();

    /**
     * 返回usage信息
     *
     * @return usage
     */
    String usage();

    /**
     * cmd options
     *
     * @return Options
     */
    Options getOptions();

    /**
     * 参数的补全
     *
     * @return Completer
     */
    ArgCompleter getArgCompleter();
}
