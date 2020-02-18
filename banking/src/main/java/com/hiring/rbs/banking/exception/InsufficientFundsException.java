package com.hiring.rbs.banking.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(final String message) {
        super(message);
    }

    public InsufficientFundsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
