package com.dd.api.client;

import com.dd.api.config.ApiConfig;
import com.dd.api.exceptions.ApiException;
import com.dd.api.utils.StringKit;
import com.dd.api.utils.TimeFormat;
import com.dd.api.utils.TimeKit;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * @author zhangzp
 */
public class ApiClient {
    private static final Logger LOG = LoggerFactory.getLogger(ApiClient.class);
    private ApiConfig config;

    private Retrofit retrofit;

    private OkHttpClient client;

    public ApiClient(ApiConfig config) {
        if (config == null || StringKit.isEmpty(config.getEndpoint())) {
            throw new RuntimeException("The APIClient params can't be empty.");
        }

        this.config = config;
        this.client = new ApiHttpClient(config).client();
        this.retrofit = new ApiRetrofit(config, client).retrofit();
    }

    public <T> T createService(final Class<T> service) {
        return this.retrofit.create(service);
    }

    /**
     * Synchronous send request
     */
    public <T> T executeSync(final Call<T> call) {
        try {
            final Response<T> response = call.execute();
            if (this.config.isPrint()) {
                this.printResponse(response);
            }
            final int status = response.code();
            final String message = response.code() + " / " + response.message();

            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new ApiException(status, message);
            }
        } catch (final IOException e) {
            throw new ApiException("APIClient executeSync exception.", e);
        }
    }

    private void printResponse(final Response response) {
        final StringBuilder responseInfo = new StringBuilder();
        responseInfo.append("\n\t========================================================");
        responseInfo.append("\n\tResponse").append("(").append(TimeKit.now(TimeFormat.LONG_DATE_PATTERN_WITH_MILLI_LINE)).append("):");
        if (response != null) {

            responseInfo.append("\n\t\t").append("URL: ").append(response.raw().request().url());
            responseInfo.append("\n\t\t").append("Status: ").append(response.code());
            responseInfo.append("\n\t\t").append("Message: ").append(response.message());
            responseInfo.append("\n\t\t").append("Body: ").append(new Gson().toJson(response.body()));

            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                try {
                    responseInfo.append("\n\t\t").append("ErrorBody: ").append(new Gson().toJson(errorBody.string()));
                } catch (Exception e) {
                    LOG.warn("Read response error body exception", e);
                }

            }
        } else {
            responseInfo.append("\n\t\t").append("\n\tRequest Error: response is null");
        }
        LOG.info(responseInfo.toString());
    }
}
