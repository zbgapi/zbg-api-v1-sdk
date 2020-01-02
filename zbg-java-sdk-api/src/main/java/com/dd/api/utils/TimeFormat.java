package com.dd.api.utils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 日期时间格式枚举类型
 * <p>
 * created by zhangzp on 2018/5/25.
 */
public enum TimeFormat {
    /**
     * 时间格式，HHmmss
     */
    TIME_PATTERN("HHmmss"),
    /**
     * 时间格式，HH:mm:ss
     */
    TIME_PATTERN_SLASH("HH:mm:ss"),
    /**
     * 时间格式，HH:mm:ss.SSS
     */
    TIME_PATTERN_WITH_MILLI_SLASH("HH:mm:ss.SSS"),
    /**
     * 时间格式，HHmmssSSS
     */
    TIME_PATTERN_WITH_MILLI("HHmmssSSS"),
    //短时间格式 年月日
    /**
     * 时间格式：yyyy-MM-dd
     */
    SHORT_DATE_PATTERN_LINE("yyyy-MM-dd"),
    /**
     * 时间格式：yyyy/MM/dd
     */
    SHORT_DATE_PATTERN_SLASH("yyyy/MM/dd"),
    /**
     * 时间格式：yyyyMMdd
     */
    SHORT_DATE_PATTERN_NONE("yyyyMMdd"),

    // ==============================================================================
    //                         长时间格式 年月日时分秒
    // ==============================================================================

    /**
     * 时间格式：标准的ISO-8601的时间格式
     */
    LONG_DATE_PATTERN_ISO("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    LONG_DATE_PATTERN_LINE("yyyy-MM-dd HH:mm:ss"),

    /**
     * 时间格式：yyyy/MM/dd HH:mm:ss
     */
    LONG_DATE_PATTERN_SLASH("yyyy/MM/dd HH:mm:ss"),
    /**
     * 时间格式：yyyyMMdd HH:mm:ss
     */
    LONG_DATE_PATTERN_NONE("yyyyMMdd HH:mm:ss"),
    /**
     * 时间格式：yyyyMMddHHmmss
     */
    LONG_DATE_PATTERN_NO_SPACE("yyyyMMddHHmmss"),

    /**
     * 长时间格式 年月日时分秒 带毫秒
     */
    LONG_DATE_PATTERN_WITH_MILLI_LINE("yyyy-MM-dd HH:mm:ss.SSS"),
    /**
     * 时间格式：yyyy/MM/dd HH:mm:ss.SSS
     */
    LONG_DATE_PATTERN_WITH_MILLI_SLASH("yyyy/MM/dd HH:mm:ss.SSS"),
    /**
     * 时间格式：yyyyMMdd HH:mm:ss.SSS
     */
    LONG_DATE_PATTERN_WITH_MILLI_NONE("yyyyMMdd HH:mm:ss.SSS"),
    /**
     * 时间格式：yyyyMMddHHmmssSSS
     */
    LONG_DATE_PATTERN_WITH_MILLI_NO_SPACE("yyyyMMddHHmmssSSS");



    protected transient DateTimeFormatter formatter;

    TimeFormat(String pattern) {
        formatter = formatter(pattern);
    }

    /**
     * 根据时间格式生产时间日期格式化类
     *
     * @param pattern 日期格式
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter formatter(String pattern) {
        return new DateTimeFormatterBuilder().append(DateTimeFormatter.ofPattern(pattern))
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }
}
