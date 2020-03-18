package com.dd.api.exceptions;

/**
 * @author zhangzp
 */
public class ApiException extends RuntimeException {

    private String code;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = String.valueOf(code);
    }

    public ApiException(String code, String message) {
        super(message);
        this.code = code;
    }


    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        if (this.code != null) {
            return "[" +this.code + "]: " + super.getMessage();
        }
        return super.getMessage();
    }

    public String getCode() {
        return code;
    }
}