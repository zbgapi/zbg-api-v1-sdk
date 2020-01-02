package com.dd.api.service.spot;

import com.dd.api.entity.commom.Page;
import com.dd.api.entity.order.param.AddOrderParam;
import com.dd.api.entity.order.param.CancelOrderParam;
import com.dd.api.entity.order.param.OrderParam;
import com.dd.api.entity.order.result.Order;
import com.dd.api.entity.order.result.Trade;

import java.math.BigDecimal;
import java.util.List;

/**
 * 币币交易
 *
 * @author zhangzp
 */
public interface OrderApiService {

    /**
     * 添加一个委托订单，此接口只提交添加请求，接口会立刻响应返回一个申请委托号，可以根据这个委托号去查询订单状态。
     *
     * @return 委托订单号
     */
    String addOrder(AddOrderParam param);

    /**
     * 添加一个买订单，此接口只提交添加请求，接口会立刻响应返回一个申请委托号，可以根据这个委托号去查询订单状态。
     *
     * @return 委托订单号
     */
    String buy(String symbol, BigDecimal price, BigDecimal amount);

    /**
     * 添加一个卖订单，此接口只提交添加请求，接口会立刻响应返回一个申请委托号，可以根据这个委托号去查询订单状态。
     *
     * @return 委托订单号
     */
    String sell(String symbol, BigDecimal price, BigDecimal amount);

    /**
     * 发送一个撤销订单的请求。
     * 此接口只提交取消请求，实际取消结果需要通过订单状态，撮合状态等接口来确认。
     *
     * @param symbol  交易对，例如：btc_usdt,eth_usdt
     * @param orderId 订单号
     */
    void cancelOrder(String symbol, String orderId);

    /**
     * 批量撤销订单
     *
     * @return 撤销订单数
     */
    Integer batchCancelOrder(CancelOrderParam param);

    /**
     * 查询当前未成交完成的订单
     */
    Page<Order> getOpenOrders(String symbol, Integer page, Integer size);

    /**
     * 搜索条件查询历史订单。
     */
    Page<Order> getOrders(OrderParam param);

    /**
     * 查询订单
     */
    Order getOrder(String symbol, String orderId);

    /**
     * 根据委托订单的成交记录
     */
    List<Trade> getTrades(String symbol, String orderId);
}
