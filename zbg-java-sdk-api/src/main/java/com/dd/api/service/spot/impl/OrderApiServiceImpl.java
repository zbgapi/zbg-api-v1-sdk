package com.dd.api.service.spot.impl;

import com.dd.api.api.OrderApi;
import com.dd.api.client.ApiClient;
import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.Page;
import com.dd.api.entity.order.param.AddOrderParam;
import com.dd.api.entity.order.param.CancelOrderParam;
import com.dd.api.entity.order.param.OrderParam;
import com.dd.api.entity.order.result.Order;
import com.dd.api.entity.order.result.Trade;
import com.dd.api.enums.ErrorCodeEnum;
import com.dd.api.enums.OrderSideEnum;
import com.dd.api.exceptions.ApiException;
import com.dd.api.service.spot.OrderApiService;
import com.dd.api.utils.StringKit;
import com.dd.api.utils.TimeFormat;
import com.dd.api.utils.TimeKit;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangzp
 */
public class OrderApiServiceImpl implements OrderApiService {
    private final ApiClient client;

    private final OrderApi api;

    public OrderApiServiceImpl(ApiConfig config) {
        this.client = new ApiClient(config);
        this.api = this.client.createService(OrderApi.class);
    }

    public OrderApi getApi() {
        return api;
    }

    @Override
    public String addOrder(AddOrderParam param) {

        return this.client.executeSync(this.api.addOrder(param));
    }

    @Override
    public String buy(@NonNull String symbol, @NonNull BigDecimal price, @NonNull BigDecimal amount) {
        AddOrderParam param = AddOrderParam.builder().symbol(symbol)
                .side(OrderSideEnum.buy)
                .price(price.stripTrailingZeros().toPlainString())
                .amount(amount.stripTrailingZeros().toPlainString())
                .build();

        return this.client.executeSync(this.api.addOrder(param));
    }

    @Override
    public String sell(@NonNull String symbol, @NonNull BigDecimal price, @NonNull BigDecimal amount) {
        AddOrderParam param = AddOrderParam.builder().symbol(symbol)
                .side(OrderSideEnum.sell)
                .price(price.stripTrailingZeros().toPlainString())
                .amount(amount.stripTrailingZeros().toPlainString())
                .build();

        return this.client.executeSync(this.api.addOrder(param));
    }

    @Override
    public void cancelOrder(@NonNull String symbol, @NonNull String orderId) {
        Map<String, String> param = new HashMap<>(4);
        param.put("symbol", symbol);
        param.put("order-id", orderId);

        this.client.executeSync(this.api.cancelOrder(param));
    }

    @Override
    public Integer batchCancelOrder(CancelOrderParam param) {
        try {
            return this.client.executeSync(this.api.batchCancelOrder(param));
        } catch (ApiException e) {
            if (ErrorCodeEnum.NO_MATCH_ENTRUST.getCode().equals(e.getCode())) {
                return 0;
            }
            throw e;
        }
    }

    @Override
    public Page<Order> getOpenOrders(@NonNull String symbol, Integer page, Integer size) {
        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 100;
        }
        return this.client.executeSync(this.api.getOpenOrders(symbol, page, size));
    }

    @Override
    public Page<Order> getOrders(OrderParam param) {
        if (StringKit.isEmpty(param.getSymbol())) {
            throw new ApiException("symbol must not be empty");
        }

        Map<String, String> map = new HashMap<>(8);
        map.put("symbol", param.getSymbol());
        if (param.getSide() != null) {
            map.put("side", param.getSide().name());
        }

        if (param.getState() != null) {
            map.put("state", param.getState().name());
        }

        if (param.getStartDate() != null) {
            map.put("start-time", TimeKit.format(param.getStartDate(), TimeFormat.SHORT_DATE_PATTERN_LINE));
        }

        if (param.getEndDate() != null) {
            map.put("end-time", TimeKit.format(param.getEndDate(), TimeFormat.SHORT_DATE_PATTERN_LINE));
        }

        if (param.getStartDate() != null && param.getEndDate() != null) {
            if (param.getStartDate().compareTo(param.getEndDate()) > 0) {
                throw new ApiException("start date must be less than or equal than end date.");
            }
        }

        Integer page = param.getPage();
        Integer size = param.getSize();
        if (page == null || page < 1) {
            page = 1;
        }

        if (size == null || size < 1) {
            size = 20;
        }

        map.put("history", String.valueOf(param.isHistory()));
        map.put("page", String.valueOf(page));
        map.put("size", String.valueOf(size));
        return this.client.executeSync(this.api.getOrders(map));
    }

    @Override
    public Order getOrder(@NonNull String symbol, @NonNull String orderId) {
        return this.client.executeSync(this.api.getOrder(symbol, orderId));
    }

    @Override
    public List<Trade> getTrades(@NonNull String symbol, @NonNull String orderId) {
        return this.client.executeSync(this.api.getTrades(symbol, orderId));
    }
}
