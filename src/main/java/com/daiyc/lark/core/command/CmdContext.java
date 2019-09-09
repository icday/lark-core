package com.daiyc.lark.core.command;

import com.daiyc.lark.core.session.Session;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author daiyc
 */
@Data
@Accessors(chain = true)
public class CmdContext {
    private CommandRepository commandRepository;

    private Session session;
}
