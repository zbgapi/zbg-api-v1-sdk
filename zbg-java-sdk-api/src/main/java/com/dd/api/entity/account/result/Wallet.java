package com.dd.api.entity.account.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class Wallet {
    /**
     * 币种名称
     */
    private String currency;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 冻结余额
     */
    private BigDecimal freeze;
    /**
     * 可用余额
     */
    private BigDecimal available;
}
