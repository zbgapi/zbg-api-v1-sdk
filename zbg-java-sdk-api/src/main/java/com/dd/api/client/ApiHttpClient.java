package com.dd.api.client;

import com.dd.api.config.ApiConfig;
import com.dd.api.constant.ApiConstants;
import com.dd.api.enums.ContentTypeEnum;
import com.dd.api.enums.HttpHeadersEnum;
import com.dd.api.enums.HttpMethodEnum;
import com.dd.api.exceptions.ApiException;
import com.dd.api.utils.DigestKit;
import com.dd.api.utils.StringKit;
import com.dd.api.utils.TimeFormat;
import com.dd.api.utils.TimeKit;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 对 OkHttpClient 调用进行再封装，对私有 API 调用，增加了接口签名
 *
 * @author zhangzp
 */
public class ApiHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(ApiHttpClient.class);

    private final ApiConfig config;

    public ApiHttpClient(ApiConfig config) {
        this.config = config;
    }


    public OkHttpClient client() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(this.config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(this.config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(this.config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(this.config.isRetryOnConnectionFailure())
                .addInterceptor(chain -> {
                    long timestamp = System.currentTimeMillis();
                    Request request = chain.request().newBuilder()
                            .headers(this.header(chain.request(), timestamp))
                            .build();

                    if (this.config.isPrint()) {
                        this.printRequest(request, timestamp);
                    }

                    return chain.proceed(request);
                });

        return builder.build();
    }

    private Headers header(Request request, long timestamp) {
        Headers.Builder builder = new Headers.Builder();
        builder.add(HttpHeadersEnum.ACCEPT.header(), ContentTypeEnum.APPLICATION_JSON.contentType())
                .add(HttpHeadersEnum.CONTENT_TYPE.header(), ContentTypeEnum.APPLICATION_JSON_UTF8.contentType());

        if (StringKit.isNotEmpty(this.config.getApiKey())) {
            builder.add(HttpHeadersEnum.API_KEY.header(), this.config.getApiKey())
                    .add(HttpHeadersEnum.TIMESTAMP.header(), String.valueOf(timestamp))
                    .add(HttpHeadersEnum.SIGN.header(), this.sign(request, timestamp))
                    .add(HttpHeadersEnum.CLIENT_TYPE.header(), ApiConstants.CLIENT_TYPE);

            if (StringKit.isNotEmpty(this.config.getPassphrase())) {
                builder.add(HttpHeadersEnum.Passphrase.header(), DigestKit.md5(timestamp + this.config.getPassphrase()));
            }
        }
        return builder.build();
    }

    private String sign(Request request, long timestamp) {

        return DigestKit.md5(assembly(request, timestamp)).toLowerCase();
    }

    private String assembly(Request request, long timestamp) {
        String method = request.method().toUpperCase();
        String content;
        if (HttpMethodEnum.POST.name().equals(method) || HttpMethodEnum.PUT.name().equals(method)) {
            content = this.body(request);
        } else {
            content = this.queryString(request);
        }
        return this.config.getApiKey() + timestamp + content + this.config.getSecretKey();
    }

    private String url(final Request request) {
        return request.url().toString();
    }

    private String queryString(final Request request) {
        final String url = this.url(request);
        String queryString = ApiConstants.EMPTY;
        if (url.contains(ApiConstants.QUESTION)) {
            queryString = url.substring(url.lastIndexOf(ApiConstants.QUESTION) + 1);
        }

        return Arrays.stream(queryString.split(ApiConstants.AT))
                .map(e -> e.replace(ApiConstants.EQUAL, ApiConstants.EMPTY))
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.joining(ApiConstants.EMPTY));
    }

    private String body(final Request request) {
        final RequestBody requestBody = request.body();
        String body = ApiConstants.EMPTY;
        if (requestBody != null) {
            try {
                final Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                body = buffer.readString(ApiConstants.UTF_8);
            } catch (IOException e) {
                throw new ApiException("Request get body io exception.", e);
            }
        }
        return body;
    }

    private void printRequest(Request request, long timestamp) {
        final String method = request.method().toUpperCase();
        final String body = this.body(request);

        final StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("\n\t========================================================");
        requestInfo.append("\n").append("\tSecret-Key: ").append(this.config.getSecretKey());
        requestInfo.append("\n\tRequest").append("(").append(TimeKit.now(TimeFormat.LONG_DATE_PATTERN_WITH_MILLI_LINE)).append("):");
        requestInfo.append("\n\t\t").append("Url: ").append(this.url(request));
        requestInfo.append("\n\t\t").append("Method: ").append(method);
        requestInfo.append("\n\t\t").append("Headers: ");
        final Headers headers = request.headers();
        if (headers.size() > 0) {
            for (final String name : headers.names()) {
                requestInfo.append("\n\t\t\t").append(name).append(": ").append(headers.get(name));
            }
        }
        requestInfo.append("\n\t\t").append("Body: ").append(body);

        if (request.headers().get(HttpHeadersEnum.SIGN.header()) != null) {
            requestInfo.append("\n\t\t").append("Hash original String: ").append(assembly(request, timestamp));
        }
        LOG.info(requestInfo.toString());
    }
}
