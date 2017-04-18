package com.transaction.exceptions;

/**
 * Created by caporegim on 18.04.2017.
 */
public class RuleNotFoundException extends RuntimeException {
    public RuleNotFoundException() {
    }

    public RuleNotFoundException(String message) {
        super(message);
    }

    public RuleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleNotFoundException(Throwable cause) {
        super(cause);
    }

    public RuleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
