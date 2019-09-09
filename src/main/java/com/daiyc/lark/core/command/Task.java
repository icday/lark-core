package com.daiyc.lark.core.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.cli.CommandLine;

/**
 * @author daiyc
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Task {
    private CommandLine commandLine;
}
