package com.dd.api.constant;

import java.nio.charset.Charset;

/**
 * @author zhangzp
 */
public interface ApiConstants {
    /**
     * zbg 默认访问域名
     */
    String DEFAULT_ENDPOINT = "https://www.zbg.com";

    /**
     * The default timeout is 30 seconds.
     */
    long TIMEOUT = 1000 * 30;

    String HEADER_NAME_LOCALE = "locale=";

    Charset UTF_8 = Charset.forName("UTF-8");

    String CLIENT_TYPE = "5";

    String QUESTION = "?";

    String SLASH = "/";

    String AT = "&";

    String EMPTY = "";

    String EQUAL = "=";
    /**
     * 接口成功返回的错误码
     */
    String RESPONSE_OK_CODE = "1";
}
