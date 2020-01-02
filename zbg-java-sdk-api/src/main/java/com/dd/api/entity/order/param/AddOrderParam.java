package com.dd.api.entity.order.param;

import com.dd.api.enums.OrderSideEnum;
import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangzp
 */
@Data
@Builder
public class AddOrderParam {
    /**
     * 委托交易对
     */
    @NotNull
    private String symbol;
    /**
     * 委托类型，buy/sell
     */
    @NotNull
    private OrderSideEnum side;
    /**
     * 委托数量
     */
    @NotNull
    private String amount;
    /**
     * 委托价格
     */
    @NotNull
    private String price;
}
