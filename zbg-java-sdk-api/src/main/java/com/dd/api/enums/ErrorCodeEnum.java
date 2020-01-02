package com.dd.api.enums;

/**
 * @author zhangzp
 */
public enum ErrorCodeEnum {
    /**
     *
     */
    NO_MATCH_ENTRUST("2028", "no match entrusts");

    private String code;
    private String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
