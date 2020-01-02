package com.dd.api.service.common;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.result.AssistPrice;
import com.dd.api.entity.commom.result.Currency;
import com.dd.api.entity.commom.result.ServerTime;
import com.dd.api.entity.commom.result.Symbol;
import com.dd.api.service.common.impl.CommonApiServiceImpl;
import com.dd.api.utils.TimeFormat;
import com.dd.api.utils.TimeKit;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author zhangzp
 */
public class CommonApiServiceTest {
    private CommonApiService commonApiService;

    @Before
    public void setUp() throws Exception {
        ApiConfig config = ApiConfig.builder()
//                .endpoint("https://www.zbg.com")
                .print(true)
                .build();
        this.commonApiService = new CommonApiServiceImpl(config);
    }

    @Test
    public void getCurrencies() {
        List<Currency> symbols = commonApiService.getCurrencies();
        assertTrue(symbols.size() > 0);
    }

    @Test
    public void getSymbols() {
        List<Symbol> symbols = commonApiService.getSymbols();
        assertTrue(symbols.size() > 0);
    }

    @Test
    public void getServerTime() {
        System.out.println(TimeKit.format(new Date(), TimeFormat.LONG_DATE_PATTERN_ISO));
        ServerTime result = commonApiService.getServerTime();
        assertTrue(result.getTimestamp() < System.currentTimeMillis());
    }

    @Test
    public void getAssistPrice() {
        AssistPrice result = commonApiService.getAssistPrice();

        assertNotNull(result.getBtc());

        result = commonApiService.getAssistPrice("usdt");

        assertNotNull(result.getBtc().get("usdt"));
        assertEquals("1", result.getUsd().get("usdt"));
    }
}