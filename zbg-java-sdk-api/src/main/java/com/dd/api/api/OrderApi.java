package com.dd.api.api;

import com.dd.api.entity.commom.Page;
import com.dd.api.entity.commom.result.HttpResult;
import com.dd.api.entity.order.param.AddOrderParam;
import com.dd.api.entity.order.param.CancelOrderParam;
import com.dd.api.entity.order.result.Order;
import com.dd.api.entity.order.result.Trade;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * 币币交易
 *
 * @author zhangzp
 */
public interface OrderApi {

    /**
     * 添加一个委托订单，此接口只提交添加请求，接口会立刻响应返回一个申请委托号，可以根据这个委托号去查询订单状态。
     *
     * @return 委托订单号
     */
    @POST("/exchange/api/v1/order/create")
    Call<String> addOrder(@Body AddOrderParam param);

    /**
     * 发送一个撤销订单的请求。
     * 此接口只提交取消请求，实际取消结果需要通过订单状态，撮合状态等接口来确认。
     * <pre>
     *     symbol	string	true	交易对，例如：btc_usdt,eth_usdt
     *     order-id	string	true	订单号
     * </pre>
     */
    @POST("/exchange/api/v1/order/cancel")
    Call<HttpResult> cancelOrder(@Body Map<String, String> param);

    /**
     * 批量撤销订单，如果没有匹配到委托单，则会抛一个错误码为2028的异常
     *
     * @return 撤销订单数
     */
    @POST("/exchange/api/v1/order/batch-cancel")
    Call<Integer> batchCancelOrder(@Body CancelOrderParam param);

    /**
     * 查询当前未成交完成的订单
     */
    @GET("/exchange/api/v1/order/open-orders")
    Call<Page<Order>> getOpenOrders(@Query("symbol") String symbol, @Query("page") Integer page, @Query("size") Integer size);

    /**
     * 搜索条件查询历史订单。
     */
    @GET("/exchange/api/v1/order/orders")
    Call<Page<Order>> getOrders(@QueryMap Map<String, String> param);

    /**
     * 查询订单
     */
    @GET("/exchange/api/v1/order/detail")
    Call<Order> getOrder(@Query("symbol") String symbol, @Query("order-id") String orderId);

    /**
     * 根据委托订单的成交记录
     */
    @GET("/exchange/api/v1/order/trades")
    Call<List<Trade>> getTrades(@Query("symbol") String symbol, @Query("order-id") String orderId);
}
