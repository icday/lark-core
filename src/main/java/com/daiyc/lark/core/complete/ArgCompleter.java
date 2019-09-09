package com.daiyc.lark.core.complete;

import java.util.Collections;
import java.util.List;

/**
 * @author daiyc
 */
public interface ArgCompleter {
    /**
     * 补全
     *
     * @param args 输入的参数列表（不包含options部分）
     * @param cursor 当前光标所在的参数index，[0, args.size()]，cursor == args.size()时表示光标处于尾部空白位置
     * @return candidates
     */
    List<CharSequence> complete(List<String> args, int cursor);

    /**
     * 是否部分补全（有需要手动输入的，无法补全的部分）
     *
     * @return boolean
     */
    default boolean partialCompleter() {
        return false;
    }

    ArgCompleter NULL = (args, cursor) -> Collections.emptyList();
}
