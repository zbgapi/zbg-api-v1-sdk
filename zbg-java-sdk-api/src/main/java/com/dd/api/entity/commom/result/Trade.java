package com.dd.api.entity.commom.result;

import com.dd.api.enums.OrderSideEnum;
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
        Trade ticker = new Trade();
        ticker.setTimestamp(Integer.valueOf(e.get(2)));
        ticker.setSymbol(e.get(3).toLowerCase());
        ticker.setSide("bid".equalsIgnoreCase(e.get(4)) ? OrderSideEnum.buy : OrderSideEnum.sell);
        ticker.setPrice(new BigDecimal(e.get(5)));
        ticker.setVolume(new BigDecimal(e.get(6)));
        return ticker;
    }

    public Date getTime() {
        return new Date(this.timestamp * 1000);
    }
}
