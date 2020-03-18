package com.dd.api.websocket.subscribe.event;

import com.dd.api.enums.OrderSideEnum;
import com.dd.api.enums.OrderStateEnum;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class OrderChange {
    /**
     * 委托单号
     */
    private String orderId;
    /**
     * 买卖类型
     */
    private OrderSideEnum side;
    /**
     * 状态
     */
    private OrderStateEnum status;
    /**
     * 创建时间戳
     */
    private long timestamp;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 委托量
     */
    private BigDecimal amount;
    /**
     * 完成量
     */
    private BigDecimal filledAmount;
    /**
     * 均价
     */
    private BigDecimal avgAmount;
    /**
     * 完成金额
     */
    private BigDecimal filledCashAmount;

    public static OrderChange valueOf(JsonArray array) {
        OrderChange change = new OrderChange();
        change.setOrderId(array.get(0).getAsString());
        change.setSide(array.get(1).getAsInt() == 1 ? OrderSideEnum.buy : OrderSideEnum.sell);
        change.setStatus(parseStatus(array.get(2).getAsInt()));
        change.setPrice(array.get(3).getAsBigDecimal());
        change.setAmount(array.get(4).getAsBigDecimal());
        change.setFilledAmount(parseBigDecimal(array.get(5).getAsString()));
        change.setFilledCashAmount(parseBigDecimal(array.get(6).getAsString()));
        change.setAvgAmount(parseBigDecimal(array.get(7).getAsString()));
        change.setTimestamp(array.get(8).getAsLong());
        return change;
    }

    private static OrderStateEnum parseStatus(int status) {
        // 0起始 1取消 2交易成功 3交易一部分
        if (0 == status) {
            return OrderStateEnum.created;
        } else if (1 == status) {
            return OrderStateEnum.canceled;
        } else if (2 == status) {
            return OrderStateEnum.filled;
        } else if (3 == status) {
            return OrderStateEnum.partial_filled;
        }
        return OrderStateEnum.unknown;
    }

    private static BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(value);
    }
}
