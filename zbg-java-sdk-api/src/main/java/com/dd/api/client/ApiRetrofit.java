package com.dd.api.client;

import com.dd.api.client.convert.MyGsonConverterFactory;
import com.dd.api.config.ApiConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author zhangzp
 */
public class ApiRetrofit {
    private ApiConfig config;
    private OkHttpClient client;

    public ApiRetrofit(ApiConfig config, OkHttpClient client) {
        this.config = config;
        this.client = client;
    }

    /**
     * Get a retrofit 2 object.
     */
    public Retrofit retrofit() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(this.client);
        builder.addConverterFactory(MyGsonConverterFactory.create());
        builder.addConverterFactory(ScalarsConverterFactory.create());
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.baseUrl(this.config.getEndpoint());
        return builder.build();
    }
}
