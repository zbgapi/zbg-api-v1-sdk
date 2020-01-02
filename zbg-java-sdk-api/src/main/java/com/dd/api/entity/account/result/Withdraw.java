package com.dd.api.entity.account.result;

import com.dd.api.enums.WithdrawStateEnum;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangzp
 */
@Data
public class Withdraw {
    /**
     * 提币 ID
     */
    @SerializedName("withdraw-id")
    private String withdrawId;

    /**
     * 币种
     */
    private String currency;

    /**
     * 地址
     */
    private String address;

    /**
     * 提币数量
     */
    private BigDecimal amount;

    /**
     * 交易哈希
     */
    @SerializedName("tx-hash")
    private String txHash;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 状态：
     * <pre>
     * reexamine	审核中
     * canceled	    已撤销
     * pass	        审批通过
     * reject	    审批拒绝
     * transferred	已打币
     * confirmed	已完成
     * </pre>
     */
    private WithdrawStateEnum state;

    /**
     * 发起时间
     */
    @SerializedName("created-at")
    private Long createdAt;

    /**
     * 审核通过时间
     */
    @SerializedName("audited-at")
    private Long auditedAt;
}
