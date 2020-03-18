package com.dd.api.enums;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhangzp
 */
public enum OrderStateEnum {
    /**
     * 部分成交
     */
    @SerializedName("partial-filled")
    partial_filled("partial-filled"),

    /**
     * 部分成交撤销
     */
    @SerializedName("partial-canceled")
    partial_canceled("partial-canceled"),

    /**
     * 完全成交,
     */
    @SerializedName("filled")
    filled("filled"),

    /**
     * 已撤销，
     */
    @SerializedName("canceled")
    canceled("canceled"),

    /**
     * 已创建（入库）
     */
    @SerializedName("created")
    created("created"),

    /**
     * 未知
     */
    @SerializedName("unknown")
    unknown("unknown");


    private String state;

    OrderStateEnum(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
