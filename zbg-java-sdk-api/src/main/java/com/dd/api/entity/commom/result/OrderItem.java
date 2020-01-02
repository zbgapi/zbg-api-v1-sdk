package com.dd.api.entity.commom.result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class OrderItem implements Comparable<OrderItem> {
    /**
     * 订单价格
     */
    private BigDecimal price;
    /**
     * 订单数量
     */
    private BigDecimal amount;

    public static OrderItem valueOf(List<String> list) {
        OrderItem item = new OrderItem();
        item.setPrice(new BigDecimal(list.get(0)));
        item.setAmount(new BigDecimal(list.get(1)));
        return item;
    }

    @Override
    public int compareTo(OrderItem o) {
        return this.price.compareTo(o.getPrice());
    }
}
