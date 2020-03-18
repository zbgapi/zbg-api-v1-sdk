package com.dd.api.exceptions;

/**
 * @author zhangzp
 */
public class SubscribeException extends Exception {

    public SubscribeException() {
    }

    public SubscribeException(String message) {
        super(message);
    }

    public SubscribeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscribeException(Throwable cause) {
        super(cause);
    }
}
