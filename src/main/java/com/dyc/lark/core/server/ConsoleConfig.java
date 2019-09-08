package com.dyc.lark.core.server;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author daiyc
 */
@Data
@Accessors(chain = true)
public class ConsoleConfig {
    private String host = "127.0.0.1";

    private int port = 8899;

    private String prompt = "> ";
}
