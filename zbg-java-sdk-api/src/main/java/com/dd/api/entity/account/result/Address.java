package com.dd.api.entity.account.result;

import lombok.Data;

/**
 * @author zhangzp
 */
@Data
public class Address {
    /**
     * 币种名称
     */
    private String currency;

    /**
     * 地址
     */
    private String address;

    /**
     * 充币地址标签
     */
    private String memo;

    /**
     * 备注
     */
    private String remark;
}
