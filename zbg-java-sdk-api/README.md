## ZBG Exchange API V1 SDK

### 1.使用技术：okhttp3 + retrofit2

### 2.依赖maven，内部使用 (用户可下载源码后上传nexus使用)

```
<dependency>
    <groupId>com.dd</groupId>
    <artifactId>zbg-java-sdk-api</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 使用

```java
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
                .apiKey("您的 api key")
                .secretKey("您的 secretKey")
                .passphrase("您的 passphrase")
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

}
```

### 与Spring结合

```java
@RestController
public class TestZBGApi {

    @Autowired
    private CommonApiService commonApiService;

    @GetMapping("/server-time")
    public ServerTime getServerTime() {
        return commonApiService.getServerTime();
    }
    
    @Bean
    public ApiConfig zbgApiConfig() {
        return ApiConfig.builder()
                        .endpoint("https://www.zbg.com")
                        .apiKey("您的 api key")
                        .secretKey("您的 secretKey")
                        .passphrase("您的 passphrase")
                        .print(true)
                        .build();
    }

    @Bean
    public CommonApiService generalAPIService(ApiConfig config) {
        return new CommonApiServiceImpl(config);
    }
```