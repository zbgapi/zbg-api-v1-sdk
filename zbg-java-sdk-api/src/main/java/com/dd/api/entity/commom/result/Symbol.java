package com.dd.api.entity.commom.result;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 返回的交易对信息
 *
 * @author zhangzp
 */
@Data
public class Symbol {
    /**
     * 交易对在本交易所的ID
     */
    private Integer id;
    /**
     * 交易对名称，一般都是返回小写字母
     */
    private String symbol;
    /**
     * 交易对中的基础币种, 如 btc_usdt 中的 btc
     */
    @SerializedName("base-currency")
    private String baseCurrency;
    /**
     * 交易对中的报价币种, 如 btc_usdt 中的 usdt
     */
    @SerializedName("quote-currency")
    private String quoteCurrency;
    /**
     * 交易对报价的精度（小数点后位数）
     */
    @SerializedName("price-precision")
    private Integer pricePrecision;
    /**
     * 交易对基础币种计数精度（小数点后位数）
     */
    @SerializedName("amount-precision")
    private Integer amountPrecision;
    /**
     * 交易区，可能值: main：主流区，innovation：创新区
     */
    @SerializedName("symbol-partition")
    private String symbolPartition;
    /**
     * 交易对状态；可能值: [online,offline,suspend]
     * <pre>
     * online - 已上线；
     * offline - 交易对已下线，不可交易；
     * suspend -- 交易暂停
     * </pre>
     */
    private String state;
    /**
     * 交易对最小下单量，默认为 0
     */
    @SerializedName("min-order-amt")
    private BigDecimal minOrderAmt;
    /**
     * 交易对最大下单量，默认 1000,0000
     */
    @SerializedName("max-order-amt")
    private BigDecimal maxOrderAmt;
}
