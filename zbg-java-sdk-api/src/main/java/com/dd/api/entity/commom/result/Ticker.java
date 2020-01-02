package com.dd.api.entity.commom.result;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class Ticker {
    /**
     * 交易对名称
     */
    private String symbol;
    /**
     * 最新成交价
     */
    private BigDecimal close;
    /**
     * 最低价
     */
    private BigDecimal low;
    /**
     * 最高价
     */
    private BigDecimal high;
    /**
     * 24小时成交量
     */
    private BigDecimal volume;
    /**
     * 24小时涨跌幅
     */
    private BigDecimal change;
    /**
     * 买一价
     */
    private BigDecimal bid;
    /**
     * 卖一价
     */
    private BigDecimal ask;
    /**
     * 24小时成交额,(单位是买方币种)
     */
    private BigDecimal amount;

    public static Ticker valueOf(List<String> datas) {
        Ticker ticker = new Ticker();
        ticker.setClose(new BigDecimal(datas.get(1)));
        ticker.setHigh(new BigDecimal(datas.get(2)));
        ticker.setLow(new BigDecimal(datas.get(3)));
        ticker.setVolume(new BigDecimal(datas.get(4)));
        ticker.setChange(new BigDecimal(datas.get(5)));
        ticker.setBid(new BigDecimal(datas.get(7)));
        ticker.setAsk(new BigDecimal(datas.get(8)));
        ticker.setAmount(new BigDecimal(datas.get(9)));
        return ticker;
    }
}
