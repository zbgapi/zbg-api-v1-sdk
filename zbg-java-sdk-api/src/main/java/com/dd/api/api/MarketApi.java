package com.dd.api.api;

import com.dd.api.entity.commom.result.TradeHistory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import java.util.Map;

/**
 * 行情数据
 *
 * @author zhangzp
 */
public interface MarketApi {
    /**
     * 返回历史K线数据。（蜡烛图）
     *
     * @param symbol   交易对名称
     * @param type     K线类型,支持1M，5M，15M，30M，1H，1D，1W
     *                 七种类型，分别代表1-30分钟，1小时，1日，1周
     * @param dataSize 返回 K 线数据条数,[1,100]
     */
    @Headers({"url_name:kline"})
    @GET("https://kline.zbg.com/api/data/v1/klines")
    Call<List<List<String>>> getKlines(@Query("marketName") String symbol, @Query("type") String type, @Query("dataSize") Integer dataSize);

    /**
     * 获取ticker信息同时提供最近24小时的交易聚合信息。
     *
     * @param symbol 交易对名称
     */
    @Headers({"url_name:kline"})
    @GET("https://kline.zbg.com/api/data/v1/ticker")
    Call<List<String>> getTicker(@Query("marketName") String symbol);

    /**
     * 获得所有交易对的 tickers，数据取值时间区间为24小时滚动。
     *
     * @param isUseMarketName 是否返回交易对名称
     */
    @Headers({"url_name:kline"})
    @GET("https://kline.zbg.com/api/data/v1/tickers")
    Call<Map<String, List<String>>> getTickers(@Query("isUseMarketName") boolean isUseMarketName);

    /**
     * 返回历史K线数据。（蜡烛图）
     *
     * @param symbol   交易对名称
     * @param dataSize 返回数据条数，默认80，最多为1000
     */
    @Headers({"url_name:kline"})
    @GET("https://kline.zbg.com/api/data/v1/trades")
    Call<List<List<String>>> getTrades(@Query("marketName") String symbol, @Query("dataSize") Integer dataSize);

    /**
     * 返回指定交易对的当前市场深度数据。
     *
     * @param symbol   交易对名称
     * @param dataSize 档位数，表示买卖各5档，最大为100
     */
    @Headers({"url_name:kline"})
    @GET("https://kline.zbg.com/api/data/v1/entrusts")
    Call<Map<String, Object>> getPriceDepth(@Query("marketName") String symbol, @Query("dataSize") Integer dataSize);

    /**
     * 查询交易对的近80历史成交记录
     *
     * @param symbol 交易对名称
     */
    @GET("/exchange/api/v1/common/trade-history/{symbol}")
    Call<List<TradeHistory>> getHistoryTrades(@Path("symbol") String symbol);

    /**
     * 返回从[trade-id]往后的最多1000历史成交记录：
     *
     * @param symbol  交易对名称
     * @param tradeId 限制的成交记录ID
     */
    @GET("/exchange/api/v1/common/trade-history/{symbol}/{trade-id}")
    Call<List<TradeHistory>> getHistoryTrades(@Path("symbol") String symbol, @Path("trade-id") String tradeId);
}
