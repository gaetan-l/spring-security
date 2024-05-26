package com.training.demo.exception;

import lombok.Getter;

@Getter
public class MalformedJsonException extends IllegalArgumentException {
    /**
     * The body that should be returned when this exception is raised.
     */
    private final String responseBody;

    public MalformedJsonException(final String message, final String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }
}
