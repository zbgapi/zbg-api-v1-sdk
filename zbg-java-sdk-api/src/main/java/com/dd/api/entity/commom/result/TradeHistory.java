package com.dd.api.entity.commom.result;

import com.dd.api.enums.OrderSideEnum;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class TradeHistory {
    @SerializedName(("trade-id"))
    private String tradeId;
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
    private BigDecimal amount;
    /**
     * 总额
     */
    private BigDecimal total;

    /**
     * 发起时间戳，毫秒
     */
    @SerializedName("created-at")
    private long createdAt;

    public Date getTime() {
        return new Date(this.createdAt);
    }
}
