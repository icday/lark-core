package com.dyc.embed.console.complete;

import lombok.Data;

/**
 * @author daiyc
 */
@Data
public class Suggestion {
    private String suggestion;

    private String completion;

    public Suggestion(String suggestion, String completion) {
        this.suggestion = suggestion;
        this.completion = completion;
    }

    public Suggestion(String suggestion) {
        this(suggestion, suggestion);
    }
}
