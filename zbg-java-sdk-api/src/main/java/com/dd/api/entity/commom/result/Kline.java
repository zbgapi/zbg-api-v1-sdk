package com.dd.api.entity.commom.result;

import com.dd.api.enums.TimeRangeEnum;
import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class Kline {
    /**
     * 交易对
     */
    private String symbol;
    /**
     * 时间戳，秒，并以此作为此K线柱的id
     */
    private int id;
    /**
     * 开盘价
     */
    private BigDecimal open;
    /**
     * 收盘价
     */
    private BigDecimal close;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 以报价币种计量的交易量
     */
    private BigDecimal volume;
    /**
     * 涨跌幅
     */
    private BigDecimal riseRate;
    /**
     * 美元汇率
     */
    private BigDecimal localCoinRate;
    /**
     * K 线周期
     */
    private TimeRangeEnum timeRange;
    /**
     * 成交额(单位是买方币种)
     */
    private BigDecimal amount;

    public static Kline valueOf(List<String> list) {
        Kline kline = new Kline();
        kline.setSymbol(list.get(2));
        kline.setId(Integer.valueOf(list.get(3)));
        kline.setOpen(new BigDecimal(list.get(4)));
        kline.setHigh(new BigDecimal(list.get(5)));
        kline.setLow(new BigDecimal(list.get(6)));
        kline.setClose(new BigDecimal(list.get(7)));
        kline.setVolume(new BigDecimal(list.get(8)));
        kline.setRiseRate(new BigDecimal(list.get(9)));
        kline.setLocalCoinRate(new BigDecimal(list.get(10)));
        kline.setTimeRange(TimeRangeEnum.timeRange(list.get(11)));
        kline.setAmount(new BigDecimal(list.get(13)));
        return kline;
    }

    public static Kline valueOf(JsonArray list) {
        Kline kline = new Kline();
        kline.setSymbol(list.get(2).getAsString());
        kline.setId(list.get(3).getAsInt());
        kline.setOpen(list.get(4).getAsBigDecimal());
        kline.setHigh(new BigDecimal(list.get(5).getAsString()));
        kline.setLow(new BigDecimal(list.get(6).getAsString()));
        kline.setClose(new BigDecimal(list.get(7).getAsString()));
        kline.setVolume(new BigDecimal(list.get(8).getAsString()));
        kline.setRiseRate(new BigDecimal(list.get(9).getAsString()));
        kline.setLocalCoinRate(new BigDecimal(list.get(10).getAsString()));
        kline.setTimeRange(TimeRangeEnum.timeRange(list.get(11).getAsString()));
        kline.setAmount(new BigDecimal(list.get(13).getAsString()));
        return kline;
    }

    /**
     * 获取 k 线的时间
     *
     * @return k 线的统计时间
     */
    public Date getTime() {
        return new Date(this.getId() * 1000);
    }
}
