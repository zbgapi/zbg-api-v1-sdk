package com.dd.api.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 日期时间工具类
 * <p>
 * created by zhangzp on 2018/5/25.
 * @author zhangzp
 */
public class TimeKit {

    /**
     * 默认的转换格式，YYYY-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = TimeFormat.LONG_DATE_PATTERN_LINE.formatter;

    private TimeKit() {
    }

    /**
     * 将字符串解析成 {@link LocalDateTime} 格式的时间， 字符串时间格式必须是 YYYY-MM-dd HH:mm:ss
     *
     * @param timeStr 时间字符串
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parseTime(String timeStr) {
        if (timeStr.contains("T")) {
            timeStr = timeStr.replace("T", " ");
        }
        return LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 将字符串解析成 {@link LocalDateTime} 格式的时间，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param timeStr 时间字符串
     * @param format  时间格式模板
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parseTime(String timeStr, TimeFormat format) {
        return LocalDateTime.parse(timeStr, format.formatter);
    }

    /**
     * 将字符串解析成 {@link LocalDateTime} 格式的时间，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param timeStr 时间字符串
     * @param format  时间格式模板
     * @return LocalDateTime 对象
     */
    public static LocalDateTime parseTime(String timeStr, DateTimeFormatter format) {
        return LocalDateTime.parse(timeStr, format);
    }

    /**
     * 将字符串解析成 {@link Date} 格式的时间
     *
     * @param timeStr 时间字符串
     * @return Date 对象
     */
    public static Date parse(String timeStr) {
        if (timeStr.contains("T")) {
            timeStr = timeStr.replace("T", " ");
        }
        TimeFormat[] timeFormats = TimeFormat.values();

        DateTimeParseException exception = null;
        for (TimeFormat timeFormat : timeFormats) {
            try {
                Instant instant = LocalDateTime.parse(timeStr, timeFormat.formatter)
                        .atZone(ZoneId.systemDefault()).toInstant();
                return Date.from(instant);
            } catch (DateTimeParseException e) {
                exception = e;
            }
        }

        if (exception != null) {
            throw exception;
        } else {
            throw new RuntimeException("Text '" + timeStr + "' could not be parsed into date.");
        }
    }

    /**
     * 将字符串解析成 {@link Date} 格式的时间， 字符串时间格式必须是 YYYY-MM-dd HH:mm:ss
     *
     * @param timeStr 时间字符串
     * @return Date 对象
     */
    public static Date parseDate(String timeStr) {
        if (timeStr.contains("T")) {
            timeStr = timeStr.replace("T", " ");
        }
        Instant instant = LocalDateTime.parse(timeStr, DEFAULT_DATETIME_FORMATTER).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 将字符串解析成 {@link Date} 格式的时间，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param timeStr 时间字符串
     * @param format  时间格式模板
     * @return Date 对象
     */
    public static Date parseDate(String timeStr, TimeFormat format) {
        return toDate(parseTime(timeStr, format));
    }

    /**
     * 将字符串解析成 {@link Date} 格式的时间，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param timeStr 时间字符串
     * @param format  时间格式模板
     * @return Date 对象
     */
    public static Date parseDate(String timeStr, String format) {
        return toDate(parseTime(timeStr, TimeFormat.formatter(format)));
    }

    /**
     * 将 {@link LocalDateTime} 对象转换成 {@link Date} 格式对象
     *
     * @param localDateTime LocalDateTime 对象
     * @return Date 对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 将 {@link LocalDate} 对象转换成 {@link Date} 格式对象
     *
     * @param localDate LocalDate 对象
     * @return Date 对象
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 转Date
     *
     * @param obj 转换对象, Date, LocalDateTime, LocalDate, String, long, int
     * @return Date
     */
    public static Date toDate(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof LocalDateTime) {
            return TimeKit.toDate((LocalDateTime) obj);
        } else if (obj instanceof LocalDate) {
            return TimeKit.toDate((LocalDate) obj);
        } else if (obj instanceof String) {
            return TimeKit.parse(obj.toString());
        } else if (obj instanceof Integer || obj instanceof Long) {
            return new Date(Long.parseLong(obj.toString()));
        } else {
            return null;
        }

    }

    /**
     * 将 {@link Date} 对象转换成 {@link LocalDateTime} 格式对象
     *
     * @param date Date 对象
     * @return LocalDateTime 对象
     */
    public static LocalDateTime toTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    /**
     * 格式日期成字符串，日期格式模版为 YYYY-MM-dd HH:mm:ss
     *
     * @param date Date 对象
     * @return 日期字符串， 如果date为空返回 "" 字符串
     */
    public static String format(Date date) {
        return format(date, TimeFormat.LONG_DATE_PATTERN_LINE);
    }

    /**
     * 按照指定时间格式模版格式日期成字符串，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param date   Date 对象
     * @param format 日期格式模版
     * @return 日期字符串， 如果date为空返回 "" 字符串
     */
    public static String format(Date date, TimeFormat format) {
        if (date == null) {
            return "";
        }

        LocalDateTime localDateTime = toTime(date);
        return localDateTime.format(format.formatter);
    }

    /**
     * 格式日期成字符串，日期格式模版为 YYYY-MM-dd HH:mm:ss
     *
     * @param time LocalDateTime 对象
     * @return 日期字符串， 如果date为空返回 "" 字符串
     */
    public static String format(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(TimeFormat.LONG_DATE_PATTERN_LINE.formatter);
    }

    /**
     * 按照指定时间格式模版格式日期成字符串，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param time   LocalDateTime 对象
     * @param format 日期格式模版
     * @return 日期字符串， 如果date为空返回 "" 字符串
     */
    public static String format(LocalDateTime time, TimeFormat format) {
        if (time == null) {
            return "";
        }

        return time.format(format.formatter);
    }

    /**
     * 按照指定时间格式模版格式日期成字符串，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @param date   LocalDateTime 对象
     * @param format 日期格式模版
     * @return 日期字符串， 如果date为空返回 "" 字符串
     */
    public static String format(LocalDate date, TimeFormat format) {
        if (date == null) {
            return "";
        }

        return date.format(format.formatter);
    }

    /**
     * 格式化字符串
     *
     * @param date   日期对象
     * @param format 时间格式模版字符串
     * @return 日期字符串
     * @throws IllegalArgumentException 如果模版非法
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }

        LocalDateTime localDateTime = toTime(date);
        return localDateTime.format(TimeFormat.formatter(format));
    }

    /**
     * 获取当前日期字符串，日期格式模版为 YYYY-MM-dd HH:mm:ss
     *
     * @return 日期字符串
     */
    public static String now() {
        return LocalDateTime.now().format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期并将其转成指定格式字符串，时间格式模版参考枚举类型 {@link TimeFormat}
     *
     * @return 日期字符串
     */
    public static String now(TimeFormat format) {
        return LocalDateTime.now().format(format.formatter);
    }

    /**
     * 获取当前日期并将其转成指定格式字符串，时间格式模版
     *
     * @return 日期字符串
     */
    public static String now(String format) {
        return LocalDateTime.now().format(TimeFormat.formatter(format));
    }

    /**
     * 获取当前日期 + delta 后日期时间字符串，日期格式模版为 YYYY-MM-dd HH:mm:ss，
     * <li>如：当前日期是：2018-07-04 15:22:32， date(-1, ChronoUnit.DAYS) return 2018-07-03 15:22:32
     *
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}
     * @return 日期字符串
     */
    public static String now(int delta, ChronoUnit unit) {
        return LocalDateTime.now().plus(delta, unit).format(DEFAULT_DATETIME_FORMATTER);
    }

    /**
     * 获取当前日期 + delta 后日期字符串，日期格式模版为 YYYY-MM-dd，
     * <li>如：当前日期是：2018-07-04， date(-1) return 2018-07-03
     *
     * @param delta 间隔数，正为加，负为减
     * @return 日期字符串
     */
    public static String date(int delta) {
        return LocalDate.now().plusDays(delta).format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter);
    }

    /**
     * 获取当前日期 + delta 后日期字符串，日期格式模版为 YYYY-MM-dd，
     * <li>如：当前日期是：2018-07-04， date(-1, ChronoUnit.DAYS) return 2018-07-03
     *
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}, 需是天以上的类型
     * @return 日期字符串
     */
    public static String date(int delta, ChronoUnit unit) {
        if (unit.getDuration().toDays() <= 0) {
            throw new IllegalArgumentException("input ChronoUnit type '" + unit + "' must larger than type '" + ChronoUnit.DAYS + "'");
        }
        return LocalDate.now().plus(delta, unit).format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter);
    }

    /**
     * 获取当前时间 + delta 后时间字符串，日期格式模版为 HH:mm:ss，
     * <li>如：当前日期是：15:22:32， date(-1) return 14:22:32
     *
     * @param delta 间隔数，正为加，负为减
     * @return 时间字符串
     */
    public static String time(int delta) {
        return LocalTime.now().plusHours(delta).format(TimeFormat.TIME_PATTERN_SLASH.formatter);
    }

    /**
     * 获取当前时间 + delta 后时间字符串，日期格式模版为 HH:mm:ss，
     * <li>如：当前日期是：15:22:32， date(-1, ChronoUnit.HOURS) return 14:22:32
     *
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}, 需是天以上的类型
     * @return 时间字符串
     */
    public static String time(int delta, ChronoUnit unit) {
        if (unit.getDuration().toDays() > 0) {
            throw new IllegalArgumentException("input chronoUnit type '" + unit + "' must smaller than type '" + ChronoUnit.DAYS + "'");
        }
        return LocalTime.now().plus(delta, unit).format(TimeFormat.TIME_PATTERN_SLASH.formatter);
    }

    /**
     * 在当前日期上的时间元素类型加上(减去)一个指定数，
     *
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}
     * @return 加(减)了指定秒数的 Date 对象
     */
    public static Date add(int delta, ChronoUnit unit) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return toDate(localDateTime.plus(delta, unit));
    }

    /**
     * 在给定日期上加上(减去)指定秒数，
     *
     * @param date    Date 对象
     * @param seconds 秒数，正为加，负为减
     * @return 加(减)了指定秒数的 Date 对象
     */
    public static Date add(Date date, long seconds) {
        LocalDateTime localDateTime = toTime(date);
        return toDate(localDateTime.plusSeconds(seconds));
    }

    /**
     * 在给定日期上的时间元素类型加上(减去)一个指定数，
     *
     * @param date  Date 对象
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}
     * @return 加(减)了指定秒数的 Date 对象
     */
    public static Date add(Date date, long delta, ChronoUnit unit) {
        LocalDateTime localDateTime = toTime(date);
        return toDate(localDateTime.plus(delta, unit));
    }

    /**
     * 获取指定时间元素上的时间
     *
     * @param date  Date 对象
     * @param field 时间元素类型， {@link ChronoField}
     * @return 时间元素的值
     */
    public static int get(Date date, ChronoField field) {
        LocalDateTime localDateTime = toTime(date);
        return localDateTime.get(field);
    }

    /**
     * 计算两个日期的时间间隔
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return {@link Duration}
     */
    public static Duration between(Date start, Date end) {
        return Duration.between(toTime(start), toTime(end));
    }

    /**
     * 计算两个日期的时间间隔
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return {@link Duration}
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    /**
     * 计算两个日期字符串的时间间隔
     *
     * @param start 开始日期字符串
     * @param end   结束日期字符串
     * @return {@link Duration}
     */
    public static Duration between(String start, String end) {
        return between(parse(start), parse(end));
    }

    /**
     * 比较两个日期的大小，
     *
     * @param date1 日期1 可为空
     * @param date2 日期2 可为空
     * @return date1 > date2 返回1，相等返回0， 小于返回-1
     */
    public static int compare(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return 0;
        } else if (date1 == null) {
            return -1;
        } else if (date2 == null) {
            return 1;
        } else {
            return date1.compareTo(date2);
        }
    }

    /**
     * 校验字符串是否是日期
     *
     * @param date 日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        try {
            parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验字符串是否是日期
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return true or false
     */
    public static boolean isDate(String date, TimeFormat format) {
        try {
            parseTime(date, format);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当天最开始的时间 2013-03-25 00:00:00
     *
     * @return 日期字符串
     */
    public static String getDayStartTime() {
        return LocalDate.now().format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter) + " 00:00:00";
    }

    public static String getDayStartTime(int day) {
        return LocalDate.now().plusDays(day).format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter) + " 00:00:00";
    }

    public static String getDateStartTime(Date date) {
        return format(date,TimeFormat.SHORT_DATE_PATTERN_LINE) + " 00:00:00";
    }

    /**
     * 获取当天最结束的时间 2013-03-25 23:59:59
     *
     * @return 日期字符串
     */
    public static String getDayEndTimeWith() {
        return LocalDate.now().format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter) + " 23:59:59";
    }

    /**
     * 获取当天最结束的时间 2013-03-25 23:59:59.999
     *
     * @return 日期字符串
     */
    public static String getDayEndTimeWithMillis() {
        return LocalDate.now().format(TimeFormat.SHORT_DATE_PATTERN_LINE.formatter) + " 23:59:59.999";
    }

    /**
     * 在当前时间上去掉指定单位后的时间
     * <pre>
     *     retain(ChronoUnit.MINUTES) = 2018-07-18 19:20:00
     *     retain(ChronoUnit.HOURS) = 2018-07-18 19:00:00
     *     retain(ChronoUnit.DAYS) = 2018-07-18 00:00:00
     *     retain(ChronoUnit.MONTHS) = 2018-07-01 00:00:00
     *     retain(ChronoUnit.YEARS) = 2018-01-01 00:00:00
     * </pre>
     *
     * @param unit 枚举 此方法支持：
     *             {@link ChronoUnit#MINUTES}，
     *             {@link ChronoUnit#HOURS},
     *             {@link ChronoUnit#DAYS},
     *             {@link ChronoUnit#MONTHS},
     *             {@link ChronoUnit#YEARS}
     * @return Date
     */
    public static Date retain(ChronoUnit unit) {
        if (null == unit) {
            return new Date();
        }

        return retain(new Date(), unit);
    }

    /**
     * 在当前时间上去掉指定单位后的时间
     * <pre>
     *     retain(ChronoUnit.MINUTES) = 2018-07-18 19:20:00
     *     retain(ChronoUnit.HOURS) = 2018-07-18 19:00:00
     *     retain(ChronoUnit.DAYS) = 2018-07-18 00:00:00
     *     retain(ChronoUnit.MONTHS) = 2018-07-01 00:00:00
     *     retain(ChronoUnit.YEARS) = 2018-01-01 00:00:00
     * </pre>
     *
     * @param unit 枚举 此方法支持：
     *             {@link ChronoUnit#MINUTES}，
     *             {@link ChronoUnit#HOURS},
     *             {@link ChronoUnit#DAYS},
     *             {@link ChronoUnit#MONTHS},
     *             {@link ChronoUnit#YEARS}
     * @return Date
     */
    public static Date retain(Date date, ChronoUnit unit) {
        if (null == unit) {
            return date;
        }

        LocalDateTime now = toTime(date);
        switch (unit) {
            case SECONDS:
                return toDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond()));
            case MINUTES:
                return toDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()));
            case HOURS:
                return toDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0));
            case DAYS:
                return toDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0));
            case MONTHS:
                return toDate(LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0));
            case YEARS:
                return toDate(LocalDateTime.of(now.getYear(), 1, 1, 0, 0));
            default:
                return toDate(now);
        }
    }

    /**
     * 获取日期中的年份
     *
     * @param date 日期对象
     * @return 年份
     */
    public static int getYear(Date date) {
        return toTime(date).getYear();
    }

    /**
     * 获取日期中的月份
     *
     * @param date 日期对象
     * @return 月份
     */
    public static int getMonth(Date date) {
        return toTime(date).getMonthValue();
    }

    /**
     * 获取日期中的日份
     *
     * @param date 日期对象
     * @return 日份
     */
    public static int getDay(Date date) {
        return toTime(date).getDayOfMonth();
    }

    /**
     * 获取日期中的星期, 如星期一就是返回1
     *
     * @param date 日期对象
     * @return 星期
     */
    public static int getWeek(Date date) {
        return toTime(date).getDayOfWeek().getValue();
    }

    /**
     * 返回小时,0~23
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        return toTime(date).getHour();
    }

    /**
     * 返回分钟
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        return toTime(date).getMinute();
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        return toTime(date).getSecond();
    }


    /**
     * 获取当前月的第一天
     *
     * @return 当月第一天日期
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(new Date());
    }

    /**
     * 获取指定日期当月的第一天
     *
     * @param date 日期对象
     * @return 当月第一天日期
     */
    public static Date getFirstDayOfMonth(Date date) {
        LocalDateTime now = toTime(date);
        LocalDateTime first = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);

        return toDate(first);
    }

    /**
     * 获取本星期的第一天日期
     *
     * @return 本星期的第一天
     */
    public static Date getFirstDayOfWeek() {
        return getFirstDayOfWeek(new Date());
    }

    /**
     * 获取指定日期的周一的日期
     *
     * @param date 日期对象
     * @return 星期的第一天
     */
    public static Date getFirstDayOfWeek(Date date) {
        LocalDateTime now = toTime(date);
        int week = now.getDayOfWeek().getValue();
        now = now.minusDays(week - 1);
        LocalDateTime first = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);

        return toDate(first);
    }

    /**
     * 获取当前月的最后一天
     *
     * @return 当月最后一天日期
     */
    public static Date getLastDayOfMonth() {
        LocalDateTime dateTime = LocalDateTime.now().plusMonths(1);
        LocalDateTime first = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0, 0);
        return toDate(first.plusNanos(-1));
    }

    /**
     * 获取指定日期当月的最后一天
     *
     * @param date 日期对象
     * @return 当月最后一天日期
     */
    public static Date getLastDayOfMonth(Date date) {
        LocalDateTime now = toTime(date);
        LocalDateTime dateTime = now.plusMonths(1);
        LocalDateTime first = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), 1, 0, 0, 0);
        return toDate(first.plusNanos(-1));
    }

    /**
     * 获取本星期的最后一天日期
     *
     * @return 本星期的最后一天
     */
    public static Date getLastDayOfWeek() {
        return getLastDayOfWeek(new Date());
    }

    /**
     * 获取某天结束时间
     * @return 该天结束时间
     */
    public static Date getEndDate(long millis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        return toDate(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX));
    }

    /**
     * 获取某天开始时间
     * @return 改天开始时间
     */
    public static Date getStartDate(long millis) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        return toDate(LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN));
    }

    /**
     * 获取当天结束时间
     * @return 当天结束时间
     */
    public static Date getEndDate() {
        return toDate(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    /**
     * 获取当天开始时间
     * @return 当天开始时间
     */
    public static Date getStartDate() {
        return toDate(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
    }

    /**
     * 获取指定日期的最后一天的日期
     *
     * @param date 日期对象
     * @return 星期的最后一天
     */
    public static Date getLastDayOfWeek(Date date) {
        LocalDateTime now = toTime(date);
        int week = now.getDayOfWeek().getValue();
        now = now.plusDays(8 - week);
        LocalDateTime first = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);

        return toDate(first.minusNanos(1));
    }

    /**
     * 创建日期对象，当前时间 + day天数
     *
     * @param day 天数，正为加，负为减
     * @return Date对象
     */
    public static Date newDate(int day) {
        return newDate(day, ChronoUnit.DAYS);
    }

    /**
     * 创建日期对象，当前时间 + delta
     *
     * @param delta 间隔数，正为加，负为减
     * @param unit  时间元素类型， {@link ChronoUnit}
     * @return Date对象
     */
    public static Date newDate(int delta, ChronoUnit unit) {
        return toDate(LocalDateTime.now().plus(delta, unit));
    }

}
