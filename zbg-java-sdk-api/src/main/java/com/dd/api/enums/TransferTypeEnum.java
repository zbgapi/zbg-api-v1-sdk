package com.dd.api.enums;

/**
 * @author zhangzp
 */
public enum TransferTypeEnum {
    /**
     * 子账号划转给母账户虚拟币
     */
    MASTER_TRANSFER_IN("master-transfer-in"),
    /**
     * 母账户划转给子账号虚拟币
     */
    MASTER_TRANSFER_OUT("master-transfer-out");

    private String type;

    TransferTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
