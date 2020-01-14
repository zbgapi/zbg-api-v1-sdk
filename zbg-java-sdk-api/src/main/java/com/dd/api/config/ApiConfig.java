package com.dd.api.config;

import com.dd.api.constant.ApiConstants;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangzp
 */
@Getter
@Setter
public class ApiConfig {

    /**
     * The user's api key provided by ZBG.
     */
    private String apiKey;
    /**
     * The user's secret key provided by ZBG. The secret key used to sign your request data.
     */
    private String secretKey;
    /**
     * The Passphrase will be provided by you to further secure your API access.
     */
    private String passphrase;
    /**
     * Rest api endpoint url.
     */
    private String endpoint = ApiConstants.DEFAULT_ENDPOINT;

    private String klineEndpoint = ApiConstants.DEFAULT_KLINE_ENDPOINT;

    /**
     * Host connection timeout.
     */
    private long connectTimeout = ApiConstants.TIMEOUT;
    /**
     * The host reads the information timeout.
     */
    private long readTimeout = ApiConstants.TIMEOUT;
    /**
     * The host writes the information timeout.
     */
    private long writeTimeout = ApiConstants.TIMEOUT;
    /**
     * Failure reconnection, default true.
     */
    private boolean retryOnConnectionFailure = true;

    /**
     * The print api information.
     */
    private boolean print;

    public ApiConfig() {
        this(ApiConstants.DEFAULT_ENDPOINT);
    }

    public ApiConfig(String endpoint) {
        super();
        this.apiKey = null;
        this.secretKey = null;
        this.passphrase = null;
        this.endpoint = endpoint;
        this.connectTimeout = ApiConstants.TIMEOUT;
        this.readTimeout = ApiConstants.TIMEOUT;
        this.writeTimeout = ApiConstants.TIMEOUT;
        this.retryOnConnectionFailure = true;
        this.print = false;
    }

    public ApiConfig(String apiKey, String secretKey, String passphrase, String endpoint, String klineEndpoint, long connectTimeout, long readTimeout, long writeTimeout, boolean retryOnConnectionFailure, boolean print) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.passphrase = passphrase;
        this.endpoint = endpoint;
        this.klineEndpoint = klineEndpoint;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.retryOnConnectionFailure = retryOnConnectionFailure;
        this.print = print;
    }

    public static ApiConfig.ApiConfigBuilder builder() {
        return new ApiConfig.ApiConfigBuilder();
    }

    public static class ApiConfigBuilder {
        private String apiKey;
        private String secretKey;
        private String passphrase;
        private String endpoint = ApiConstants.DEFAULT_ENDPOINT;
        private String klineEndpoint = ApiConstants.DEFAULT_KLINE_ENDPOINT;
        private long connectTimeout = ApiConstants.TIMEOUT;
        private long readTimeout = ApiConstants.TIMEOUT;
        private long writeTimeout = ApiConstants.TIMEOUT;
        private boolean retryOnConnectionFailure = true;
        private boolean print;

        ApiConfigBuilder() {
        }

        public ApiConfig.ApiConfigBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public ApiConfig.ApiConfigBuilder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public ApiConfig.ApiConfigBuilder passphrase(String passphrase) {
            this.passphrase = passphrase;
            return this;
        }

        public ApiConfig.ApiConfigBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public ApiConfig.ApiConfigBuilder klineEndpoint(String klineEndpoint) {
            this.klineEndpoint = klineEndpoint;
            return this;
        }

        public ApiConfig.ApiConfigBuilder connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public ApiConfig.ApiConfigBuilder readTimeout(long readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public ApiConfig.ApiConfigBuilder writeTimeout(long writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        public ApiConfig.ApiConfigBuilder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }

        public ApiConfig.ApiConfigBuilder print(boolean print) {
            this.print = print;
            return this;
        }

        public ApiConfig build() {
            return new ApiConfig(this.apiKey, this.secretKey, this.passphrase, this.endpoint, this.klineEndpoint,
                    this.connectTimeout, this.readTimeout, this.writeTimeout, this.retryOnConnectionFailure, this.print);
        }

        @Override
        public String toString() {
            return "ApiConfig.ApiConfigBuilder(apiKey=" + this.apiKey +
                    ", secretKey=" + this.secretKey +
                    ", passphrase=" + this.passphrase +
                    ", endpoint=" + this.endpoint +
                    ", klineEndpoint=" + this.klineEndpoint +
                    ", connectTimeout=" + this.connectTimeout +
                    ", readTimeout=" + this.readTimeout +
                    ", writeTimeout=" + this.writeTimeout +
                    ", retryOnConnectionFailure=" + this.retryOnConnectionFailure +
                    ", print=" + this.print + ")";
        }
    }
}
