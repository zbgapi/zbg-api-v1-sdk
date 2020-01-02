package com.dd.api.entity.commom.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 返回的币种信息
 *
 * @author zhangzp
 */
@Data
public class Currency {
    /**
     * 币种在本交易所的ID
     */
    private Integer id;
    /**
     * 币种名称，一般都是返回小写字母
     */
    private String name;
    /**
     * 是否可以进行提币，true/false
     */
    @SerializedName("draw-flag")
    private boolean drawFlag;
    /**
     * 提币手续费
     */
    @SerializedName("draw-fee")
    private String drawFee;
    /**
     * 一次可提币最大限制
     */
    @SerializedName("once-draw-limit")
    private Integer onceDrawLimit;
    /**
     * 一次提币最小限制
     */
    @SerializedName("min-draw-limit")
    private BigDecimal minDrawLimit;
    /**
     * 每日提币最大限制
     */
    @SerializedName("daily-draw-limit")
    private BigDecimal dailyDrawLimit;

}
