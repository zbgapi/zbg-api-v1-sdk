package com.dd.api.entity.commom.result;

import com.dd.api.enums.OrderSideEnum;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class Trade {

    /**
     * 成交时间戳，秒级别
     */
    private int timestamp;
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 买卖类型 buy/sell
     */
    private OrderSideEnum side;
    /**
     * 成交价格
     */
    private BigDecimal price;
    /**
     * 交易数量
     */
    private BigDecimal volume;

    public static Trade valueOf(List<String> e) {
        Trade trade = new Trade();
        trade.setTimestamp(Integer.valueOf(e.get(2)));
        trade.setSymbol(e.get(3).toLowerCase());
        trade.setSide("bid".equalsIgnoreCase(e.get(4)) ? OrderSideEnum.buy : OrderSideEnum.sell);
        trade.setPrice(new BigDecimal(e.get(5)));
        trade.setVolume(new BigDecimal(e.get(6)));
        return trade;
    }

    public static Trade valueOf(JsonArray e) {
        Trade trade = new Trade();
        trade.setTimestamp(e.get(2).getAsInt());
        trade.setSymbol(e.get(3).getAsString().toLowerCase());
        trade.setSide("bid".equalsIgnoreCase(e.get(4).getAsString()) ? OrderSideEnum.buy : OrderSideEnum.sell);
        trade.setPrice(e.get(5).getAsBigDecimal());
        trade.setVolume(e.get(6).getAsBigDecimal());
        return trade;
    }

    public Date getTime() {
        return new Date(this.timestamp * 1000);
    }
}
