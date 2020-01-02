package com.dd.api.api;

import com.dd.api.entity.account.param.WithdrawParam;
import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Withdraw;
import com.dd.api.entity.commom.Page;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * @author zhangzp
 */
public interface WithdrawApi {

    /**
     * 获取币种的在本平台填写的充币地址。
     *
     * @param currency 币种名称
     */
    @GET("/exchange/api/v1/account/withdraw/address")
    Call<List<Address>> getWithdrawAddress(@Query("currency") String currency);

    /**
     * 提币
     */
    @POST("/exchange/api/v1/account/withdraw/create")
    Call<String> withdraw(@Body WithdrawParam param);

    /**
     * 取消提币
     *
     * @param withdrawId 提币记录ID
     */
    @POST("/exchange/api/v1/account/withdraw/cancel/{withdraw-id}")
    Call<String> cancelWithdraw(@Path("withdraw-id") String withdrawId);

    /**
     * 查询用户在本平台的提币记录。
     *
     * @param currency 币种名称
     * @param direct   返回记录排序方向， “prev” （升序）or “next” （降序）默认为“next” （降序）
     * @param page     页码
     * @param size     查询记录大小，取值 [1,500]
     */
    @GET("/exchange/api/v1/account/withdraw/history")
    Call<Page<Withdraw>> getWithdrawHistory(@Query("currency") String currency,
                                            @Query("direct") String direct,
                                            @Query("page") int page,
                                            @Query("size") int size);
}
