package com.dd.api.service.common;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.result.*;
import com.dd.api.enums.TimeRangeEnum;
import com.dd.api.service.common.impl.MarketApiServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangzp
 */
public class MarketApiServiceTest {
    protected MarketApiService marketApiService;

    @Before
    public void setUp() throws Exception {
        ApiConfig config = ApiConfig.builder()
                .endpoint("https://www.zbgpro.net")
                .print(true)
                .build();
        this.marketApiService = new MarketApiServiceImpl(config);
    }

    @Test
    public void getKlines() {
        List<Kline> klines = this.marketApiService.getKlines("btc_usdt", TimeRangeEnum.ONE_DAY, 10);
        System.out.println(klines);
    }

    @Test
    public void getTicker() {

        Ticker result = this.marketApiService.getTicker("btc_usdt");
        System.out.println(result);
    }

    @Test
    public void getTickers() {
        List<Ticker> result = this.marketApiService.getTickers();
        System.out.println(result);
    }

    @Test
    public void getTrades() {
        List<Trade> result = this.marketApiService.getTrades("btc_usdt", 4);
        System.out.println(result);
        System.out.println(result.get(0).getTime());
    }

    @Test
    public void getOrders() {
        PriceDepth result = this.marketApiService.getPriceDepth("btc_usdt", 4);
        System.out.println(result);
        System.out.println(result.getAsks(true));
        System.out.println(result.getAsks(false));
    }

    @Test
    public void getHistoryTrades() {
        List<TradeHistory> result = this.marketApiService.getHistoryTrades("btc_usdt");
//        System.out.println(result);
        System.out.println(result.get(0).getCreatedAt() < result.get(1).getCreatedAt());

        result = this.marketApiService.getHistoryTrades("btc_usdt", "T6612252948145643520");
//        System.out.println(result);
    }

}