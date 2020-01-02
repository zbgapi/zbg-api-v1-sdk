package com.dd.api.api;

import com.dd.api.entity.commom.result.AssistPrice;
import com.dd.api.entity.commom.result.Currency;
import com.dd.api.entity.commom.result.HttpResult;
import com.dd.api.entity.commom.result.Symbol;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

/**
 * @author zhangzp
 */
public interface CommonApi {

    @GET("/exchange/api/v1/common/currencys")
    Call<List<Currency>> getCurrencies();

    @GET("/exchange/api/v1/common/symbols")
    Call<List<Symbol>> getSymbols();

    @GET("/exchange/api/v1/common/timestamp")
    Call<Long> getSystemTime();

    @GET("/exchange/api/v1/common/assist-price")
    Call<AssistPrice> getAssistPrice(@Query("currencys") String currencys);

}
