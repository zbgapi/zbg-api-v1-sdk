package com.dd.api.enums;

/**
 * @author zhangzp
 */

public enum HttpHeadersEnum {

    /**
     *
     */
    API_KEY("Apiid"),

    TIMESTAMP("Timestamp"),

    Passphrase("Passphrase"),

    SIGN("Sign"),

    ACCEPT("Accept"),

    CONTENT_TYPE("Content-Type"),

    CLIENT_TYPE("Clienttype");


    private String header;

    HttpHeadersEnum(String header) {
        this.header = header;
    }

    public String header() {
        return header;
    }
}