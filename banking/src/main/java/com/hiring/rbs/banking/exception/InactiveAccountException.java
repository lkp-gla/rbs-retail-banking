package com.hiring.rbs.banking.exception;

public class InactiveAccountException extends RuntimeException {

    public InactiveAccountException() {
        super();
    }

    public InactiveAccountException(String message) {
        super(message);
    }

    public InactiveAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
