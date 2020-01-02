package com.dd.api.entity.commom.result;

import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangzp
 */
@Data
public class Order {
    /**
     * 成交时间戳，秒级别
     */
    private int timestamp;

    /**
     * 卖盘列表 按价格倒序
     */
    private List<OrderItem> asks;

    /**
     * 买盘列表， 按价格倒序
     */
    private List<OrderItem> bids;

    public Date getTime() {
        return new Date(this.timestamp * 1000);
    }

    public List<OrderItem> getAsks(boolean isDesc) {
        if (isDesc) {
            return asks;
        }

        return asks.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public List<OrderItem> getBids(boolean isDesc) {
        if (isDesc) {
            return bids;
        }

        return bids.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }
}
