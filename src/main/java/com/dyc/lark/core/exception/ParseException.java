package com.dyc.lark.core.exception;

/**
 * @author daiyc
 */
public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
