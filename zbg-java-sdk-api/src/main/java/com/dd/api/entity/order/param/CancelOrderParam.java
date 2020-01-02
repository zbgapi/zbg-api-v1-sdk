package com.dd.api.entity.order.param;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * @author zhangzp
 */
@Data
@Builder
public class CancelOrderParam {
    /**
     * 交易对，例如：btc_usdt,eth_usdt
     */
    @NonNull
    private String symbol;
    /**
     * 订单类型，buy/sell
     */
    private String side;

    /**
     * 订单号， 多个一个逗号隔开
     */
    @SerializedName("order-ids")
    private String orderIds;

    /**
     * 委托价格区间取消：取消单价>=price-from的委托
     */
    @SerializedName("price-from")
    private String priceFrom;

    /**
     * 委托价格区间取消：取消单价<=price-to
     */
    @SerializedName("price-to")
    private String priceTo;
}
