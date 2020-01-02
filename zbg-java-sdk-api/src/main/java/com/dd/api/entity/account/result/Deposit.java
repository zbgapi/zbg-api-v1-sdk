package com.dd.api.entity.account.result;

import com.dd.api.enums.DepositTypeEnum;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class Deposit {
    /**
     * 充币 ID
     */
    @SerializedName("deposit-id")
    private String depositId;

    /**
     * 币种
     */
    private String currency;

    /**
     * 地址
     */
    private String address;

    /**
     * 充币数量
     */
    private BigDecimal amount;

    /**
     * 交易哈希
     */
    @SerializedName("tx-hash")
    private String txHash;

    /**
     * 确认次数
     */
    @SerializedName("confirm-times")
    private Integer confirmTimes;

    /**
     * 状态：confirming：确认中，completed：完成
     */
    private String state;

    /**
     * 充币类型:
     * <pre>
     *      blockchain	区块链转入
     *      system	系统充值
     *      recharge	法币充值
     *      transfer	商户之间互相转账
     * </pre>
     */
    private DepositTypeEnum type;

    /**
     * 发起时间
     */
    @SerializedName("created-at")
    private Long createdAt;

    /**
     * 入账时间6次区块确认时间点
     */
    @SerializedName("confirmed-at")
    private Long confirmedAt;
}
