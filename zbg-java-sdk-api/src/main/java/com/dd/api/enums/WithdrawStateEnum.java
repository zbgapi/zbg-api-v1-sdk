package com.dd.api.enums;

/**
 * @author zhangzp
 */
public enum WithdrawStateEnum {
    /**
     * 审核中
     */
    reexamine,
    /**
     * 已撤销
     */
    canceled,
    /**
     * 审批通过
     */
    pass,
    /**
     * 审批拒绝
     */
    reject,
    /**
     * 已打币
     */
    transferred,
    /**
     * 已完成
     */
    confirmed;
}
