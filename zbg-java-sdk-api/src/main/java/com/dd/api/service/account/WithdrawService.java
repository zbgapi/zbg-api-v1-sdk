package com.dd.api.service.account;

import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Withdraw;
import com.dd.api.entity.commom.Page;
import retrofit2.http.GET;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币等
 *
 * @author zhangzp
 */
public interface WithdrawService {

    /**
     * 获取币种的在本平台填写的充币地址。
     *
     * @param currency 币种名称
     */
    List<Address> getWithdrawAddress(String currency);

    /**
     * 提币
     *
     * @param currency 币种名称
     * @param address  提币地址
     * @param amount   提币数量
     */
    String withdraw(String currency, String address, BigDecimal amount);

    /**
     * 取消提币
     *
     * @param withdrawId 提币记录ID
     */
    String cancelWithdraw(String withdrawId);

    /**
     * 查询用户在本平台的提币记录。
     *
     * @param currency 币种名称
     * @param isDesc   返回记录排序方向， 默认降序
     * @param page     页码
     * @param size     查询记录大小，取值 [1,500]
     */
    Page<Withdraw> getWithdrawHistory(String currency, boolean isDesc, int page, int size);
}
