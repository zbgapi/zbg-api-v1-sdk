package com.dd.api.service.common;

import com.dd.api.entity.commom.result.*;
import com.dd.api.enums.TimeRangeEnum;

import java.util.List;

/**
 * @author zhangzp
 */
public interface MarketApiService {

    /**
     * 返回历史K线数据。（蜡烛图）
     *
     * @param symbol   交易对名称
     * @param type     K线类型,支持1M，5M，15M，30M，1H，1D，1W
     *                 七种类型，分别代表1-30分钟，1小时，1日，1周
     * @param dataSize 返回 K 线数据条数,[1,100]
     */
    List<Kline> getKlines(String symbol, TimeRangeEnum type, int dataSize);

    /**
     * 获取ticker信息同时提供最近24小时的交易聚合信息。
     *
     * @param symbol 交易对名称
     */
    Ticker getTicker(String symbol);

    /**
     * 获得所有交易对的 tickers，数据取值时间区间为24小时滚动。
     */
    List<Ticker> getTickers();

    /**
     * 返回历史K线数据。（蜡烛图）
     *
     * @param symbol   交易对名称
     * @param dataSize 返回数据条数，默认80，最多为1000
     */
    List<Trade> getTrades(String symbol, Integer dataSize);

    /**
     * 返回指定交易对的当前市场深度数据。
     *
     * @param symbol   交易对名称
     * @param dataSize 档位数，表示买卖各5档，最大为100
     */
    Order getOrders(String symbol, Integer dataSize);

    /**
     * 查询交易对的近80历史成交记录
     *
     * @param symbol 交易对名称
     */
    List<TradeHistory> getHistoryTrades(String symbol);

    /**
     * 返回从[trade-id]往后的最多1000历史成交记录：
     *
     * @param symbol  交易对名称
     * @param tradeId 限制的成交记录ID
     */
    List<TradeHistory> getHistoryTrades(String symbol, String tradeId);
}
