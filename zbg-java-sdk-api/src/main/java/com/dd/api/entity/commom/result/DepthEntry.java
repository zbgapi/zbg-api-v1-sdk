package com.dd.api.entity.commom.result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class DepthEntry implements Comparable<DepthEntry> {
    /**
     * 订单价格
     */
    private BigDecimal price;
    /**
     * 订单数量
     */
    private BigDecimal amount;

    public static DepthEntry valueOf(List<String> list) {
        DepthEntry item = new DepthEntry();
        item.setPrice(new BigDecimal(list.get(0)));
        item.setAmount(new BigDecimal(list.get(1)));
        return item;
    }

    @Override
    public int compareTo(DepthEntry o) {
        return this.price.compareTo(o.getPrice());
    }
}
