package com.dd.api.entity.account.param;

import lombok.Builder;
import lombok.Data;

/**
 * @author zhangzp
 */
@Data
@Builder
public class WithdrawParam {
    /**
     * 提币币种
     */
    private String currency;

    /**
     * 提币地址
     */
    private String address;

    /**
     * 提币数量
     */
    private String amount;

}
