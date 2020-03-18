package com.dd.api.entity.commom.result;

import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangzp
 */
@Setter
@ToString
public class PriceDepth {
    /**
     * 成交时间戳，秒级别
     */
    private long timestamp;
    /**
     * 交易对
     */
    private String symbol;

    /**
     * 卖盘列表 按价格倒序
     */
    private List<DepthEntry> asks;

    /**
     * 买盘列表， 按价格倒序
     */
    private List<DepthEntry> bids;

    public Date getTime() {
        return new Date(this.timestamp * 1000);
    }

    public List<DepthEntry> getAsks(boolean isDesc) {
        if (isDesc) {
            return asks;
        }

        return asks.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public List<DepthEntry> getBids(boolean isDesc) {
        if (isDesc) {
            return bids;
        }

        return bids.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }
}
