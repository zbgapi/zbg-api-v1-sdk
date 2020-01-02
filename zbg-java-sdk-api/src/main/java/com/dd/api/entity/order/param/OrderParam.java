package com.dd.api.entity.order.param;

import com.dd.api.enums.OrderSideEnum;
import com.dd.api.enums.OrderStateEnum;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * 参数格式：
 * <table border=1>
 * <thead>
 * <tr><th><span>参数</span></th><th><span>数据类型</span></th><th><span>是否必须</span></th><th><span>描述</span></th></tr>
 * </thead>
 * <tbody>
 * <tr>
 * <td><span>symbol</span></td><td><span>string</span></td><td><span>true</span></td><td><span>交易对，例如：btc_usdt,eth_usdt</span></td>
 * </tr>
 * <tr>
 * <td><span>side</span></td><td><span>string</span></td><td><span>false</span></td><td><span>订单类型，buy/sell</span></td>
 * </tr>
 * <tr>
 * <td><span>state</span></td><td><span>string</span></td><td><span>false</span></td><td><span>状态，有效状态：partial-filled: 部分成交, </span><br><span> partial-canceled: 部分成交撤销,</span><br><span> filled: 完全成交, </span><br><span> canceled: 已撤销，</span><br><span> created: 已创建（入库）</span></td>
 * </tr>
 * <tr>
 * <td><span>start-date</span></td><td><span>string</span></td><td><span>false</span></td><td><span>查询开始日期, 日期格式yyyy-mm-dd</span></td>
 * </tr>
 * <tr>
 * <td><span>end-date</span></td><td><span>string</span></td><td><span>false</span></td><td><span>查询结束日期, 日期格式yyyy-mm-dd</span></td>
 * </tr>
 * <tr>
 * <td><span>history</span></td><td><span>boolean</span></td><td><span>false</span></td><td><span>是否查询历史记录，默认flase。</span></td>
 * </tr>
 * <tr>
 * <td><span>page</span></td><td><span>int</span></td><td><span>false</span></td><td><span>页码，默认 1</span></td>
 * </tr>
 * <tr>
 * <td><span>size</span></td><td><span>int</span></td><td><span>false</span></td><td><span>返回订单的数量， 默认20 最大值 100</span></td>
 * </tr>
 * </tbody>
 * </table>
 *
 * @author zhangzp
 */
@Data
@Builder
public class OrderParam {

    /**
     * 交易对
     */
    @NotNull
    private String symbol;
    /**
     * 订单类型，buy/sell
     */
    private OrderSideEnum side;
    /**
     * 状态，有效状态：
     * <pre>
     *     partial-filled: 部分成交,
     *     partial-canceled: 部分成交撤销,
     *     filled: 完全成交,
     *     canceled: 已撤销
     * </pre>
     */
    private OrderStateEnum state;
    /**
     * 查询开始日期, 日期格式yyyy-mm-dd
     */
    @SerializedName("start-date")
    private LocalDate startDate;
    /**
     * 查询结束日期, 日期格式yyyy-mm-dd
     */
    @SerializedName("end-date")
    private LocalDate endDate;
    /**
     * 是否查询历史记录，默认flase。系统会将历史数据进行拆分，将时间过久的记录移除到备份库中，
     * 如果想查询这些数据，可将该字段置成 true
     */
    private boolean history;

    private Integer page;

    private Integer size;
}
