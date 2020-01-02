package com.dd.api.enums;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * web socket 订阅主题
 *
 * @author zhangzp
 */
public enum SocketTopicEnum {
    /**
     * k线主题
     *
     * @see TimeRangeEnum
     */
    KLINE("{symbol-id}_KLINE_{period}_{symbol}", 3),
    /**
     * 市场深度
     */
    DEPTH("{symbol-id}_ENTRUST_ADD_{symbol}", 2),
    /**
     * 交易记录，首次最多返回100条数据
     */
    TRADE("{symbol-id}_TRADE_{symbol}", 2),
    /**
     * 市场24H行情数据。可选值：ALL 或 交易对ID，
     */
    TICKERS("{symbol-id}_TRADE_STATISTIC_24H", 1),
    /**
     * 订单更新
     */
    ORDER_CHANGE("{symbol-id}_RECORD_ADD_{user-id}_{symbol}", 3),;

    private String topicPattern;

    private int paramSize;

    SocketTopicEnum(String topic, int paramSize) {
        this.topicPattern = topic;
        this.paramSize = paramSize;
    }

    public String getTopicPattern() {
        return topicPattern;
    }

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)}");

    public String format(Object... params) {
        if (params == null || params.length != getParamSize()) {
            throw new IllegalArgumentException("The number of parameters is inconsistent");
        }

        String targetString = getTopicPattern();
        Matcher matcher = PATTERN.matcher(getTopicPattern());
        int index = 0;
        while (matcher.find()) {
            String key = matcher.group();
            targetString = targetString.replace(key, params[index++].toString());
        }

        return targetString;
    }

    public int getParamSize() {
        return paramSize;
    }
}
