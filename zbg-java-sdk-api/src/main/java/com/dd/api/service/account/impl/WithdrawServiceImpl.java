package com.dd.api.service.account.impl;

import com.dd.api.api.WithdrawApi;
import com.dd.api.client.ApiClient;
import com.dd.api.config.ApiConfig;
import com.dd.api.entity.account.param.WithdrawParam;
import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Withdraw;
import com.dd.api.entity.commom.Page;
import com.dd.api.service.account.WithdrawService;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
public class WithdrawServiceImpl implements WithdrawService {
    private ApiClient client;
    private WithdrawApi api;

    public WithdrawServiceImpl(ApiConfig config) {
        this.client = new ApiClient(config);
        this.api = this.client.createService(WithdrawApi.class);
    }

    public WithdrawApi getApi() {
        return api;
    }

    @Override
    public List<Address> getWithdrawAddress(@NonNull String currency) {
        return this.client.executeSync(this.api.getWithdrawAddress(currency));
    }

    @Override
    public String withdraw(@NonNull String currency, @NonNull String address, @NonNull BigDecimal amount) {
        WithdrawParam param = WithdrawParam.builder()
                .currency(currency)
                .address(address)
                .amount(amount.stripTrailingZeros().toPlainString())
                .build();
        return this.client.executeSync(this.api.withdraw(param));
    }

    @Override
    public String cancelWithdraw(@NonNull String withdrawId) {
        return this.client.executeSync(this.api.cancelWithdraw(withdrawId));
    }

    @Override
    public Page<Withdraw> getWithdrawHistory(@NonNull String currency, boolean isDesc, int page, int size) {
        String direct = isDesc ? "next" : "prev";
        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 100;
        }
        return this.client.executeSync(this.api.getWithdrawHistory(currency, direct, page, size));
    }
}
