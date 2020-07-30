package com.dd.api.constant;

import java.nio.charset.Charset;

/**
 * @author zhangzp
 */
public interface ApiConstants {
    /**
     * zbg 默认访问域名
     */
    String DEFAULT_ENDPOINT = "https://www.zbg.live";
    /**
     * zbg K线 默认访问域名
     */
    String DEFAULT_KLINE_ENDPOINT = "https://kline.zbg.live";
    /**
     * zbg 默认 ws 地址
     */
    String DEFAULT_WS_ENDPOINT = "wss:/kline.zbg.live/websocket";

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
