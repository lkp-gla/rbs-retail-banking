package com.hiring.rbs.banking.exception;

public class ActiveAccountException extends RuntimeException {
    public ActiveAccountException() {
        super();
    }

    public ActiveAccountException(String message) {
        super(message);
    }

    public ActiveAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
