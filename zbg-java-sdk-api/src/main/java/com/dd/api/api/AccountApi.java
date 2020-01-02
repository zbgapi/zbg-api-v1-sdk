package com.dd.api.api;

import com.dd.api.entity.account.param.TransferParam;
import com.dd.api.entity.account.result.Account;
import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Deposit;
import com.dd.api.entity.account.result.Wallet;
import com.dd.api.entity.commom.Page;
import com.dd.api.entity.commom.result.HttpResult;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author zhangzp
 */
public interface AccountApi {
    /**
     * 查询当前用户信息及其子账号列表
     */
    @GET("/exchange/api/v1/account/accounts")
    Call<Account> getAccount();

    /**
     * 查询当前用户的资产信息。
     */
    @GET("/exchange/api/v1/account/balance")
    Call<List<Wallet>> getWallets();

    /**
     * 查询当前用户指定币种的的资产信息。
     *
     * @param currency 币种名称
     */
    @GET("/exchange/api/v1/account/balance/{currency}")
    Call<Wallet> getWallet(@Path("currency") String currency);

    /**
     * 母账户查询其下所有子账号的各币种汇总余额。
     */
    @GET("/exchange/api/v1/account/sub/aggregate-balance")
    Call<List<Wallet>> getAggregateWallets();

    /**
     * 母账户查询其下指定子账号的各币种余额。
     *
     * @param subUid 子账号 userId
     */
    @GET("/exchange/api/v1/account/sub/balance/{sub-uid}")
    Call<List<Wallet>> getSubWallets(@Path("sub-uid") String subUid);

    /**
     * 母账户执行母子账户间的资产划转。
     */
    @POST("/exchange/api/v1/account/sub/transfer")
    Call<HttpResult> transfer(@Body TransferParam param);

    /**
     * 生成并获取币种的在本平台的充币地址。
     *
     * @param currency 币种名称
     */
    @GET("/exchange/api/v1/account/deposit/address")
    Call<Address> getDepositAddress(@Query("currency") String currency);

    /**
     * 查询用户指定币种的在本平台的充币记录。
     *
     * @param currency 币种名称
     * @param direct   返回记录排序方向， “prev” （升序）or “next” （降序）默认为“next” （降序）
     * @param page     页码
     * @param size     查询记录大小，取值 [1,500]
     */
    @GET("/exchange/api/v1/account/deposit/history")
    Call<Page<Deposit>> getDepositHistory(@Query("currency") String currency,
                                          @Query("direct") String direct,
                                          @Query("page") int page,
                                          @Query("size") int size);

}
