package com.dd.api.service.spot;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.commom.Page;
import com.dd.api.entity.order.param.AddOrderParam;
import com.dd.api.entity.order.param.CancelOrderParam;
import com.dd.api.entity.order.param.OrderParam;
import com.dd.api.entity.order.result.Order;
import com.dd.api.entity.order.result.Trade;
import com.dd.api.enums.OrderSideEnum;
import com.dd.api.service.spot.impl.OrderApiServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhangzp
 */
public class OrderApiServiceTest {
    private OrderApiService orderApiService;
    @Before
    public void setUp() throws Exception {
        ApiConfig config = ApiConfig.builder()
                .endpoint("https://www.zbg.com")
                .apiKey("7sNIR1ngJ9c7sNIR1ngJ9d")
                .secretKey("867f185e869553b1e8692557959d5e88")
//                .endpoint("http://179.zbg.com")
//                .apiKey("7sTtBMNPNdA7sTtBMNPNdB")
//                .secretKey("f8f7f6f190cc723ba8f8f90b90ca4bf4")
                .passphrase("123654")
                .print(true)
                .build();
        this.orderApiService = new OrderApiServiceImpl(config);
    }

    @Test
    public void addOrder() {
        AddOrderParam param = AddOrderParam.builder()
                .symbol("zt_usdt")
                .side(OrderSideEnum.buy)
                .price("0.0371")
                .amount("100")
                .build();
        String orderId = this.orderApiService.addOrder(param);

        Order order = this.orderApiService.getOrder("zt_usdt", orderId);
        System.out.println(order.getState());
    }

    @Test
    public void buy() {
        String orderId = this.orderApiService.buy("zt_usdt", new BigDecimal("0.0371"), new BigDecimal("100"));
        System.out.println(orderId);
    }

    @Test
    public void sell() {
        String orderId = this.orderApiService.sell("zt_usdt", new BigDecimal("0.0381"), new BigDecimal("100"));
        System.out.println(orderId);
    }

    @Test
    public void cancelOrder() {
        this.orderApiService.cancelOrder("zt_usdt", "E6612601654321623040");
    }

    @Test
    public void batchCancelOrder() {
        CancelOrderParam param = CancelOrderParam.builder()
                .symbol("zt_usdt")
                .priceFrom("0.037")
                .build();
        Integer count = this.orderApiService.batchCancelOrder(param);
        System.out.println(count);
    }

    @Test
    public void getOpenOrders() {
        Page<Order> page = this.orderApiService.getOpenOrders("zt_usdt", 1, 20);
        System.out.println(page);
    }

    @Test
    public void getOrders() {
        OrderParam param = OrderParam.builder()
                .symbol("zt_usdt")
                .startDate(LocalDate.of(2019, 11, 1))
                .build();
        Page<Order> page = this.orderApiService.getOrders(param);
        System.out.println(page);
    }

    @Test
    public void getOrder() {
        Order order = this.orderApiService.getOrder("zt_usdt", "E6612601654321623040");
        System.out.println(order.getState());
    }

    @Test
    public void getTrades() {
        List<Trade> trades = this.orderApiService.getTrades("zt_usdt", "E6607835625473191936");
        System.out.println(trades);
    }
}