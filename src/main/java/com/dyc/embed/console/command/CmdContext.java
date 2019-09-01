package com.dyc.embed.console.command;

import com.dyc.embed.console.session.Session;
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
