package com.dd.api.websocket.subscribe.event;

import com.dd.api.enums.OrderSideEnum;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class PriceDepthItem {
    /**
     * 成交时间戳，秒级别
     */
    private long timestamp;

    private String symbol;

    /**
     * buy/sell
     */
    private OrderSideEnum side;
    /**
     * 订单价格
     */
    private BigDecimal price;
    /**
     * 订单数量
     */
    private BigDecimal amount;

    public static PriceDepthItem valueOf(JsonArray list) {
        PriceDepthItem item = new PriceDepthItem();
        item.setTimestamp(list.get(2).getAsLong());
        item.setSymbol(list.get(3).getAsString());
        item.setSide("BID".equalsIgnoreCase(list.get(4).getAsString()) ? OrderSideEnum.buy : OrderSideEnum.sell);
        item.setPrice(list.get(5).getAsBigDecimal());
        item.setAmount(list.get(6).getAsBigDecimal());
        return item;
    }
}
