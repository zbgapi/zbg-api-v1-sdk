package com.dd.api.entity.order.result;

import com.dd.api.enums.OrderSideEnum;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class Trade {
    /**
     * 订单成交记录ID
     */
    @SerializedName("trade-id")
    private String tradeId;

    /**
     * 订单ID
     */
    @SerializedName("order-id")
    private String orderId;

    /**
     * 撮合对象的订单 ID
     */
    @SerializedName("match-id")
    private String matchId;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 成交价
     */
    private BigDecimal price;

    /**
     * 主动单类型， buy/sell
     */
    private OrderSideEnum side;

    /**
     * 成交数量
     */
    @SerializedName("filled-amount")
    private BigDecimal filledAmount;

    /**
     * 成交手续费
     */
    @SerializedName("filled-fees")
    private BigDecimal filledFees;

    /**
     * 成交角色	maker,taker
     */
    private String role;

    /**
     * 发起时间
     */
    @SerializedName("created-at")
    private Long createdAt;
}
