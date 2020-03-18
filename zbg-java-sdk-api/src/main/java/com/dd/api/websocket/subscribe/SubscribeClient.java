package com.dd.api.websocket.subscribe;

import com.dd.api.enums.TimeRangeEnum;
import com.dd.api.websocket.subscribe.event.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * 订阅客户端
 *
 * @author zhangzp
 */
public interface SubscribeClient {
    Logger log = LoggerFactory.getLogger(SubscribeClient.class);

    /**
     * 订阅 K 线，订阅首次返回1000条数据
     *
     * @param symbol       市场名称
     * @param type         K线周期，{@link TimeRangeEnum}
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @return id
     */
    default int subscribeKline(String symbol, TimeRangeEnum type, Consumer<KlineSubscribeEvent> callback, Consumer<Throwable> errorHandler) {
        return subscribeKline(symbol, type, callback, errorHandler, 1000);
    }

    /**
     * 订阅 K 线，订阅首次返回1000条数据，默认的错误处理方式是打印日志
     *
     * @param symbol   市场名称
     * @param type     K线周期，{@link TimeRangeEnum}
     * @param callback 数据处理回调
     * @return id
     */
    default int subscribeKline(String symbol, TimeRangeEnum type, Consumer<KlineSubscribeEvent> callback) {
        return subscribeKline(symbol, type, callback, e -> log.error("Subscribe kline occur error", e), 1000);
    }

    /**
     * 订阅 K 线，订阅首次返回1000条数据
     *
     * @param symbol       市场名称
     * @param type         K线周期，{@link TimeRangeEnum}
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @param initDataSize 首次返回的数据数目
     * @return id
     */
    int subscribeKline(String symbol, TimeRangeEnum type, Consumer<KlineSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize);

    default int subscribePriceDepth(String symbol, Consumer<PriceDepthSubscribeEvent> callback) {
        return subscribePriceDepth(symbol, callback, e -> log.error("Subscribe price-depth occur error", e));
    }

    /**
     * 订阅 市场深度
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @return id
     */
    int subscribePriceDepth(String symbol, Consumer<PriceDepthSubscribeEvent> callback, Consumer<Throwable> errorHandler);

    /**
     * 订阅 交易记录
     *
     * @param symbol   市场名称
     * @param callback 数据处理回调
     * @return id
     */
    default int subscribeTrade(String symbol, Consumer<TradeSubscribeEvent> callback) {
        return subscribeTrade(symbol, callback, e -> log.error("Subscribe trade occur error", e), 100);
    }

    /**
     * 订阅 交易记录
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @return id
     */
    default int subscribeTrade(String symbol, Consumer<TradeSubscribeEvent> callback, Consumer<Throwable> errorHandler) {
        return subscribeTrade(symbol, callback, errorHandler, 20);
    }

    /**
     * 订阅 交易记录
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @param initDataSize 首次返回的数据数目 最大100
     * @return id
     */
    int subscribeTrade(String symbol, Consumer<TradeSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize);

    default int subscribeTicker(String symbol, Consumer<TickerSubscribeEvent> callback) {
        return subscribeTicker(symbol, callback, e -> log.error("Subscribe ticker occur error", e));
    }

    /**
     * 订阅 市场24H行情数据
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @return id
     */
    int subscribeTicker(String symbol, Consumer<TickerSubscribeEvent> callback, Consumer<Throwable> errorHandler);

    /**
     * 订阅 订单更新
     *
     * @param symbol   市场名称
     * @param callback 数据处理回调
     * @return id
     */
    default int subscribeOrderUpdate(String symbol, Consumer<OrderChangeSubscribeEvent> callback) {
        return subscribeOrderUpdate(symbol, callback, e -> log.error("Subscribe order-change occur error", e), 100);
    }

    /**
     * 订阅 订单更新
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @return id
     */
    default int subscribeOrderUpdate(String symbol, Consumer<OrderChangeSubscribeEvent> callback, Consumer<Throwable> errorHandler) {
        return subscribeOrderUpdate(symbol, callback, errorHandler, 20);
    }

    /**
     * 订阅 订单更新
     *
     * @param symbol       市场名称
     * @param callback     数据处理回调
     * @param errorHandler 错误处理
     * @param initDataSize 首次返回的数据数目 最大100
     * @return id
     */
    int subscribeOrderUpdate(String symbol, Consumer<OrderChangeSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize);

    /**
     * 取消订阅
     *
     * @param id 订阅时返回的ID
     */
    void unsubscribe(int id);
}
