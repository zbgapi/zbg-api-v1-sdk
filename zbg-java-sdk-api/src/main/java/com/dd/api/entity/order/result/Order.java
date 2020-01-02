package com.dd.api.entity.order.result;

import com.dd.api.enums.OrderSideEnum;
import com.dd.api.enums.OrderStateEnum;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class Order {
    /**
     * 订单ID
     */
    @SerializedName("order-id")
    private String orderId;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 委托价格
     */
    private String price;
    /**
     * 买卖类型，buy/sell
     */
    private OrderSideEnum side;
    /**
     * 订单委托数量
     */
    private BigDecimal amount;
    /**
     * 订单中已成交部分的数量
     */
    @SerializedName("filled-amount")
    private BigDecimal filledAmount;
    /**
     * 订单中可成交部分的数量
     */
    @SerializedName("available-amount")
    private BigDecimal availableAmount;
    /**
     * 订单中已成交部分的数量
     */
    @SerializedName("filled-cash-amount")
    private BigDecimal filledCashAmount;
    /**
     * 订单状态，包括：
     * <pre>
     * partial-filled: 部分成交,
     * partial-canceled: 部分成交撤销,
     * filled: 完全成交,
     * canceled: 已撤销，
     * created: 已创建（入库）
     * </pre>
     */
    private OrderStateEnum state;
    /**
     * 发起时间
     */
    @SerializedName("created-at")
    private Long createdAt;
}
