package com.dd.api.service.account;

import com.dd.api.entity.account.result.Account;
import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Deposit;
import com.dd.api.entity.account.result.Wallet;
import com.dd.api.entity.commom.Page;
import com.dd.api.enums.TransferTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
public interface AccountService {
    /**
     * 获取用户自己的账户信息
     */
    Account getAccount();

    /**
     * 查询当前用户的资产信息。
     */
    List<Wallet> getWallets();

    /**
     * 查询当前用户指定币种的的资产信息。
     *
     * @param currency 币种名称
     */
    Wallet getWallet(String currency);

    /**
     * 母账户查询其下所有子账号的各币种汇总余额。
     */
    List<Wallet> getAggregateWallets();

    /**
     * 母账户查询其下指定子账号的各币种余额。
     *
     * @param subUid 子账号 userId
     */
    List<Wallet> getSubWallets(String subUid);

    /**
     * 母账户执行母子账户间的资产划转。
     *
     * @param subUid   子账号 userID
     * @param currency 币种名称
     * @param amount   数量
     * @param type     划转类型：<br/>
     *                 master-transfer-in  : 子账号划转给母账户虚拟币<br/>
     *                 master-transfer-out : 母账户划转给子账号虚拟币
     */
    void transfer(String subUid, String currency, BigDecimal amount, TransferTypeEnum type);

    /**
     * 生成并获取币种的在本平台的充币地址。
     *
     * @param currency 币种名称
     */
    Address getDepositAddress(String currency);

    /**
     * 查询用户指定币种的在本平台的充币记录。
     *
     * @param currency 币种名称
     * @param isDesc   返回记录排序方向， 默认降序
     * @param page     页码
     * @param size     查询记录大小，取值 [1,500]
     */
    Page<Deposit> getDepositHistory(String currency, boolean isDesc, int page, int size);

}
