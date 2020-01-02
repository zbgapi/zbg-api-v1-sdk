package com.dd.api.enums;

import com.google.gson.annotations.SerializedName;

/**
 * K 线周期枚举类
 *
 * @author zhangzp
 */
public enum TimeRangeEnum {
    /**
     * 1分钟
     */
    @SerializedName("1M")
    ONE_MINUTE("1M"),
    /**
     * 5分钟
     */
    @SerializedName("5M")
    FIVE_MINUTE("5M"),
    /**
     * 15分钟
     */
    @SerializedName("15M")
    FIFTEEN_MINUTE("15M"),
    /**
     * 30分钟
     */
    @SerializedName("30M")
    THIRTY_MINUTE("30M"),
    /**
     * 1小时
     */
    @SerializedName("1H")
    ONE_HOUR("1H"),
    /**
     * 1天
     */
    @SerializedName("1D")
    ONE_DAY("1D"),
    /**
     * 1周
     */
    @SerializedName("1W")
    ONE_WEEK("1W"),;

    private String timeRange;

    TimeRangeEnum(String timeRange) {
        this.timeRange = timeRange;
    }


    public String getTimeRange() {
        return timeRange;
    }

    public static TimeRangeEnum timeRange(String timeRange) {
        for (TimeRangeEnum timeRangeEnum : values()) {
            if (timeRangeEnum.timeRange.equals(timeRange)) {
                return timeRangeEnum;
            }
        }

        return null;
    }
}
