package com.dyc.lark.core.util;

/**
 * @author daiyc
 */
public class Preconditions {
    public static void checkState(boolean condition) {
        if (!condition) {
            throw new IllegalStateException();
        }
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
