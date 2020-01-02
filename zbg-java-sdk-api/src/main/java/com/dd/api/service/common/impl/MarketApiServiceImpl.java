package com.dd.api.service.common.impl;

import com.dd.api.api.MarketApi;
import com.dd.api.client.ApiClient;
import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.result.*;
import com.dd.api.enums.TimeRangeEnum;
import com.dd.api.service.common.MarketApiService;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangzp
 */
public class MarketApiServiceImpl implements MarketApiService {

    private ApiClient client;
    private MarketApi api;

    public MarketApiServiceImpl(ApiConfig config) {
        this.client = new ApiClient(config);
        this.api = this.client.createService(MarketApi.class);
    }

    public MarketApi getApi() {
        return api;
    }

    @Override
    public List<Kline> getKlines(@NonNull String symbol, TimeRangeEnum type, int dataSize) {
        List<List<String>> dataList = this.client.executeSync(this.api.getKlines(symbol, type.getTimeRange(), dataSize));

        return dataList.stream().map(Kline::valueOf).collect(Collectors.toList());
    }

    @Override
    public Ticker getTicker(@NonNull String symbol) {
        List<String> dataList = this.client.executeSync(this.api.getTicker(symbol.toLowerCase()));

        Ticker ticker = Ticker.valueOf(dataList);
        ticker.setSymbol(symbol.toLowerCase());
        return ticker;
    }


    @Override
    public List<Ticker> getTickers() {
        Map<String, List<String>> dataList = this.client.executeSync(this.api.getTickers(true));

        return dataList.entrySet().stream()
                .map(e -> {
                    Ticker ticker = Ticker.valueOf(e.getValue());
                    ticker.setSymbol(e.getKey().toLowerCase());
                    return ticker;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Trade> getTrades(@NonNull String symbol, Integer dataSize) {
        List<List<String>> dataList = this.client.executeSync(this.api.getTrades(symbol, dataSize));

        return dataList.stream().map(Trade::valueOf).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Order getOrders(@NonNull String symbol, Integer dataSize) {
        Map<String, Object> dataMap = this.client.executeSync(this.api.getOrders(symbol, dataSize));
        Integer timestamp = Integer.valueOf(dataMap.get("timestamp").toString());
        List<List<String>> asks = (List<List<String>>) dataMap.get("asks");
        List<List<String>> bids = (List<List<String>>) dataMap.get("bids");

        Order order = new Order();
        order.setTimestamp(timestamp);
        order.setAsks(asks.stream().map(OrderItem::valueOf).collect(Collectors.toList()));
        order.setBids(bids.stream().map(OrderItem::valueOf).collect(Collectors.toList()));
        return order;
    }

    @Override
    public List<TradeHistory> getHistoryTrades(@NonNull String symbol) {
        return this.client.executeSync(this.api.getHistoryTrades(symbol));
    }

    @Override
    public List<TradeHistory> getHistoryTrades(@NonNull String symbol, @NonNull String tradeId) {
        return this.client.executeSync(this.api.getHistoryTrades(symbol, tradeId));
    }
}
