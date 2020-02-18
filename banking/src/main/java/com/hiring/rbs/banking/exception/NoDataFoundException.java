package com.hiring.rbs.banking.exception;

public class NoDataFoundException extends RuntimeException {

    public NoDataFoundException() {
        super();
    }

    public NoDataFoundException(final String message) {
        super(message);
    }

    public NoDataFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
