package com.dd.api.service.common.impl;

import com.dd.api.client.ApiClient;
import com.dd.api.api.CommonApi;
import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.result.*;
import com.dd.api.service.common.CommonApiService;
import com.dd.api.utils.TimeFormat;
import com.dd.api.utils.TimeKit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzp
 */
public class CommonApiServiceImpl implements CommonApiService {

    private ApiClient client;
    private CommonApi api;

    private Map<String, Symbol> symbolMap = new HashMap<>();

    public CommonApiServiceImpl(ApiConfig config) {
        this.client = new ApiClient(config);
        this.api = this.client.createService(CommonApi.class);
    }

    public CommonApi getCommonApi() {
        return api;
    }

    @Override
    public List<Currency> getCurrencies() {
        return this.client.executeSync(this.api.getCurrencies());
    }

    @Override
    public List<Symbol> getSymbols() {
        return this.client.executeSync(this.api.getSymbols());
    }

    @Override
    public Symbol getSymbol(String name) {
        if (symbolMap.isEmpty()) {
            List<Symbol> symbols = getSymbols();
            for (Symbol symbol : symbols) {
                symbolMap.put(symbol.getSymbol().toLowerCase(), symbol);
            }
        }

        if (symbolMap.containsKey(name.toLowerCase())) {
            return symbolMap.get(name);
        }
        return null;
    }

    @Override
    public ServerTime getServerTime() {
        Long timestamp = this.client.executeSync(this.api.getSystemTime());
        ServerTime serverTime = new ServerTime();
        serverTime.setTimestamp(timestamp);
        serverTime.setIso(TimeKit.format(new Date(timestamp), TimeFormat.LONG_DATE_PATTERN_ISO));
        return serverTime;
    }

    @Override
    public AssistPrice getAssistPrice(String... currencies) {
        String currency = null;
        if (currencies != null) {
            currency = String.join(",", currencies);
        }
        return this.client.executeSync(this.api.getAssistPrice(currency));
    }
}
