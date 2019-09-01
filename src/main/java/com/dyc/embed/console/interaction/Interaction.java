package com.dyc.embed.console.interaction;

/**
 * @author daiyc
 */
public interface Interaction {
    void install();

    void uninstall();

    void active();

    void inactive();

    default RetCode loop() {
        return RetCode.EXIT;
    }
}
