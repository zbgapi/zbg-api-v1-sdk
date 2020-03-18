package com.dd.api.websocket.subscribe;

import com.dd.api.config.ApiConfig;
import com.dd.api.enums.TimeRangeEnum;
import com.dd.api.websocket.subscribe.event.KlineSubscribeEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

/**
 * @author zhangzp
 */
public class SubscribeClientImplTest {
    private SubscribeClient subscribeClient;

    @Before
    public void setUp() {
        ApiConfig config = ApiConfig.builder()
                .apiKey("7uunMaHwK807uunMaHwK81")
                .secretKey("c46ac8c9ab29652cc9500feeb59ded1b")
                .endpoint("http://178.zbg.com")
                .wsEndpoint("ws://192.168.1.178:28080/websocket")
                .print(false)
                .build();
        subscribeClient = new SubscribeClientImpl(config);
    }

    @Test
    public void subscribeKline() {
        subscribeClient.subscribeKline("zt_usdt", TimeRangeEnum.ONE_MINUTE, System.out::println, null, 2);

        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void subscribePriceDepth() {
        subscribeClient.subscribePriceDepth("btc_usdt", System.out::println);

        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void subscribeTrade() {
        int id = subscribeClient.subscribeTrade("btc_usdt", System.out::println);
        System.out.println(id);

        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void subscribeTicker() {
        int id = subscribeClient.subscribeTicker("all", System.out::println);
        System.out.println(id);

        id = subscribeClient.subscribeTicker("btc_usdt", System.out::println);
        System.out.println(id);

        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void subscribeOrderUpdate() {

        int id = subscribeClient.subscribeOrderUpdate("zt_usdt", System.out::println);
        System.out.println(id);

        int id2 = subscribeClient.subscribeTrade("btc_usdt", System.out::println);
        System.out.println(id2);

        try {
            Thread.sleep(5000);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        subscribeClient.unsubscribe(id);

        try {
            Thread.sleep(100000);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}