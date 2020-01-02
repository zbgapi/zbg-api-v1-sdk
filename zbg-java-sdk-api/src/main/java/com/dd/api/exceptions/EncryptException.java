package com.dd.api.exceptions;

public class EncryptException extends RuntimeException {
    public EncryptException(Throwable t) {
        super(t);
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable t) {
        super(message, t);
    }
}