package com.dd.api.entity.account.param;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangzp
 */
@Data
@Builder
public class TransferParam {

    /**
     * 子账号 userID
     */
    @SerializedName("sub-uid")
    private String subUid;
    /**
     * 币种名称
     */
    private String currency;
    /**
     * 数量
     */
    private String amount;
    /**
     * 划转类型：<br/>
     * master-transfer-in  : 子账号划转给母账户虚拟币<br/>
     * master-transfer-out : 母账户划转给子账号虚拟币
     */
    private String type;
}
