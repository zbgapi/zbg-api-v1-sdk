package com.dd.api.websocket.subscribe;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.account.result.Account;
import com.dd.api.entity.commom.result.Symbol;
import com.dd.api.enums.SocketTopicEnum;
import com.dd.api.enums.TimeRangeEnum;
import com.dd.api.exceptions.ApiException;
import com.dd.api.exceptions.SubscribeException;
import com.dd.api.service.account.AccountService;
import com.dd.api.service.account.impl.AccountServiceImpl;
import com.dd.api.service.common.CommonApiService;
import com.dd.api.service.common.impl.CommonApiServiceImpl;
import com.dd.api.websocket.WebSocket;
import com.dd.api.websocket.WebSocketClient;
import com.dd.api.websocket.WebSocketListener;
import com.dd.api.websocket.subscribe.event.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author zhangzp
 */
@Slf4j
@Data
public class SubscribeClientImpl implements SubscribeClient {
    private static AtomicInteger ID_COUNT = new AtomicInteger(1);
    private static Map<Integer, ConnectionAdapter> CONNECTION_MAP = new HashMap<>(64);
    private String url;
    private CommonApiService commonApiService;
    private AccountService accountService;

    public SubscribeClientImpl() {
    }

    public SubscribeClientImpl(ApiConfig apiConfig) {
        this.url = apiConfig.getWsEndpoint();
        this.commonApiService = new CommonApiServiceImpl(apiConfig);
        this.accountService = new AccountServiceImpl(apiConfig);
    }

    public SubscribeClientImpl(CommonApiService commonApiService, AccountService accountService) {
        this.commonApiService = commonApiService;
        this.accountService = accountService;
    }

    public SubscribeClientImpl(CommonApiService commonApiService, AccountService accountService, String url) {
        this.url = url;
        this.commonApiService = commonApiService;
        this.accountService = accountService;
    }

    @Override
    public int subscribeKline(String symbol, TimeRangeEnum type, Consumer<KlineSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize) {
        Symbol market = commonApiService.getSymbol(symbol);

        if (market == null) {
            throw new ApiException("symbol[" + symbol + "] is not exist");
        }

        String topic = SocketTopicEnum.KLINE.format(market.getId(), type.getTimeRange(), symbol.toUpperCase());

        ConnectionAdapter<KlineSubscribeEvent> adapter = new ConnectionAdapter<>();
        adapter.topic = topic;
        adapter.initDataSize = initDataSize;
        adapter.jsonParser = new KlineSubscribeEvent();
        adapter.callback = callback;
        adapter.errorHandler = errorHandler;

        adapter.connect();
        return adapter.id;
    }

    @Override
    public int subscribePriceDepth(String symbol, Consumer<PriceDepthSubscribeEvent> callback, Consumer<Throwable> errorHandler) {
        Symbol market = commonApiService.getSymbol(symbol);

        if (market == null) {
            throw new ApiException("symbol[" + symbol + "] is not exist");
        }

        String topic = SocketTopicEnum.DEPTH.format(market.getId(), symbol.toUpperCase());

        ConnectionAdapter<PriceDepthSubscribeEvent> adapter = new ConnectionAdapter<>();
        adapter.topic = topic;
        adapter.jsonParser = new PriceDepthSubscribeEvent();
        adapter.callback = callback;
        adapter.errorHandler = errorHandler;

        adapter.connect();
        return adapter.id;
    }

    @Override
    public int subscribeTrade(String symbol, Consumer<TradeSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize) {
        Symbol market = commonApiService.getSymbol(symbol);

        if (market == null) {
            throw new ApiException("symbol[" + symbol + "] is not exist");
        }

        String topic = SocketTopicEnum.TRADE.format(market.getId(), symbol.toUpperCase());

        ConnectionAdapter<TradeSubscribeEvent> adapter = new ConnectionAdapter<>();
        adapter.topic = topic;
        adapter.jsonParser = new TradeSubscribeEvent();
        adapter.callback = callback;
        adapter.errorHandler = errorHandler;
        adapter.initDataSize = initDataSize;

        adapter.connect();
        return adapter.id;
    }

    @Override
    public int subscribeTicker(String symbol, Consumer<TickerSubscribeEvent> callback, Consumer<Throwable> errorHandler) {
        String symbolId;
        if ("all".equalsIgnoreCase(symbol)) {
            symbolId = "ALL";
        } else {
            Symbol market = commonApiService.getSymbol(symbol);

            if (market == null) {
                throw new ApiException("symbol[" + symbol + "] is not exist");
            }

            symbolId = market.getId().toString();
        }


        String topic = SocketTopicEnum.TICKERS.format(symbolId);

        ConnectionAdapter<TickerSubscribeEvent> adapter = new ConnectionAdapter<>();
        adapter.topic = topic;
        adapter.jsonParser = new TickerSubscribeEvent();
        adapter.callback = callback;
        adapter.errorHandler = errorHandler;

        adapter.connect();
        return adapter.id;
    }

    @Override
    public int subscribeOrderUpdate(String symbol, Consumer<OrderChangeSubscribeEvent> callback, Consumer<Throwable> errorHandler, int initDataSize) {
        Account account = this.accountService.getAccount();

        Symbol market = commonApiService.getSymbol(symbol);

        if (market == null) {
            throw new ApiException("symbol[" + symbol + "] is not exist");
        }

        String topic = SocketTopicEnum.ORDER_CHANGE.format(market.getId(), account.getUserId(), symbol.toUpperCase());

        ConnectionAdapter<OrderChangeSubscribeEvent> adapter = new ConnectionAdapter<>();
        adapter.topic = topic;
        adapter.jsonParser = new OrderChangeSubscribeEvent();
        adapter.callback = callback;
        adapter.errorHandler = errorHandler;
        adapter.initDataSize = initDataSize;

        adapter.connect();
        return adapter.id;
    }

    @Override
    public void unsubscribe(int id) {
        ConnectionAdapter adapter = CONNECTION_MAP.get(id);
        if (adapter == null) {
            return;
        }

        adapter.disconnect();
    }

    class ConnectionAdapter<T extends SubscribeEvent> {
        private Integer id;
        private String topic;
        private int initDataSize;
        private SubscribeEvent<T> jsonParser;
        private Consumer<T> callback;
        private Consumer<Throwable> errorHandler;
        private WebSocketClient webSocketClient;

        public ConnectionAdapter() {
            this.id = ID_COUNT.getAndIncrement();

            this.webSocketClient = new WebSocketClient(url, new WebSocketListener() {
                private ScheduledExecutorService service;

                @Override
                public void onOpen(WebSocket webSocket) {
                    // 定时发送 pong 命令，防止长时间没有数据导致连接断开
                    service = Executors.newSingleThreadScheduledExecutor();

                    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
                    service.scheduleAtFixedRate(webSocket::ping, 5, 10, TimeUnit.SECONDS);

                    webSocket.subscribe(topic, initDataSize);
                }

                @Override
                public void onMessage(WebSocket webSocket, String message) {
                    if (message.contains("code")) {
                        onFailure(webSocket, new SubscribeException());
                        return;
                    }

                    T res;
                    try {
                        res = jsonParser.parse(message);
                    } catch (Exception e) {
                        log.error("[Sub][" + id + "] Failed to parse server's response", e);
                        this.onFailure(webSocket, new SubscribeException("Failed to parse server's response: ", e));
                        return;
                    }

                    try {
                        if (callback != null) {
                            callback.accept(res);
                        }
                    } catch (Exception e) {
                        log.error("[Sub][" + id + "] Failed to call the callback method", e);
                        this.onFailure(webSocket, new SubscribeException("Process error, You should capture the exception in your error handler", e));
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t) {
                    log.error("[Sub][" + id + "] subscribe error.", t);

                    if (errorHandler != null) {
                        errorHandler.accept(t);
                    }
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    log.error("[Sub][" + id + "] Connection is closing due to error, code:" + code + ", reason:" + reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    log.error("[Sub][" + id + "] Connection is closed due to error, code:" + code + ", reason:" + reason);
                }
            });
        }

        public void connect() {
            CONNECTION_MAP.put(id, this);
            this.webSocketClient.connect();
        }

        public void disconnect() {
            this.webSocketClient.unsubscribe(topic);
            this.webSocketClient.close();
        }
    }
}
