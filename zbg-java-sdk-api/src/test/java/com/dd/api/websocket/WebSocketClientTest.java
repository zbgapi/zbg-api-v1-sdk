package com.dd.api.websocket;

import com.dd.api.enums.SocketTopicEnum;
import com.dd.api.enums.TimeRangeEnum;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzp
 */
public class WebSocketClientTest {
    private WebSocketClient webSocketClient;

    @Before
    public void setUp() {

        this.webSocketClient = new WebSocketClient(new WebSocketListener() {
            ScheduledExecutorService service;

            @Override
            public void onOpen(WebSocket webSocket) {
                // 定时发送 pong 命令，防止长时间没有数据导致连接断开
                service = Executors.newSingleThreadScheduledExecutor();
                // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
                service.scheduleAtFixedRate(webSocket::ping, 25, 25, TimeUnit.SECONDS);

            }

            @Override
            public void onMessage(WebSocket webSocket, String message) {
                System.out.println("message:" + message);
            }

            @Override
            public void onPong(WebSocket ws) {
                System.out.println("onPong");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        this.webSocketClient.connect();
    }

    @Test
    public void subscribeKline() {
        String topic = SocketTopicEnum.KLINE.format(336, TimeRangeEnum.ONE_DAY.getTimeRange(), "zt_usdt");
        this.webSocketClient.subscribe(topic, 1);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        this.webSocketClient.unsubscribe(topic);

        System.out.println("==========");


        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(10000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }

    @Test
    public void subscribeTickers() {
        String topic = SocketTopicEnum.TICKERS.format("ALL");
        this.webSocketClient.subscribe(topic, 10);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(1000000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }

    @Test
    public void subscribeTicker() {
        String topic = SocketTopicEnum.TICKERS.format("336");
        this.webSocketClient.subscribe(topic, 10);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(1000000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }

    @Test
    public void subscribeDepth() {
        String topic = SocketTopicEnum.DEPTH.format("336", "ZT_USDT");
        this.webSocketClient.subscribe(topic, 10);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(1000000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }

    @Test
    public void subscribeTrade() {
        String topic = SocketTopicEnum.TRADE.format("336", "ZT_USDT");
        this.webSocketClient.subscribe(topic, 20);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(1000000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }

    @Test
    public void subscribeOrderChange() {
        String topic = SocketTopicEnum.ORDER_CHANGE.format("336", "7eOUtLBFXTU", "ZT_USDT");
        this.webSocketClient.subscribe(topic, 20);
        //为保证测试方法不停，需要让线程延迟
        try {
            Thread.sleep(1000000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("====end======");
    }
}