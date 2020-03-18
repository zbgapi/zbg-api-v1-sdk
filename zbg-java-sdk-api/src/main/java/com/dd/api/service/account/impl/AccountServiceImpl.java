package com.dd.api.service.account.impl;

import com.dd.api.api.AccountApi;
import com.dd.api.client.ApiClient;
import com.dd.api.config.ApiConfig;
import com.dd.api.entity.account.param.TransferParam;
import com.dd.api.entity.account.result.Account;
import com.dd.api.entity.account.result.Address;
import com.dd.api.entity.account.result.Deposit;
import com.dd.api.entity.account.result.Wallet;
import com.dd.api.entity.commom.Page;
import com.dd.api.enums.TransferTypeEnum;
import com.dd.api.service.account.AccountService;
import com.dd.api.service.common.impl.CommonApiServiceImpl;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhangzp
 */
public class AccountServiceImpl implements AccountService {
    private ApiClient client;
    private AccountApi api;

    public AccountServiceImpl(ApiConfig config) {
        this.client = new ApiClient(config);
        this.api = this.client.createService(AccountApi.class);
    }

    public AccountApi getApi() {
        return api;
    }

    @Override
    public Account getAccount() {
        return this.client.executeSync(this.api.getAccount());
    }

    @Override
    public List<Wallet> getWallets() {
        return this.client.executeSync(this.api.getWallets());
    }

    @Override
    public Wallet getWallet(@NonNull String currency) {
        return this.client.executeSync(this.api.getWallet(currency));
    }

    @Override
    public List<Wallet> getAggregateWallets() {
        return this.client.executeSync(this.api.getAggregateWallets());
    }

    @Override
    public List<Wallet> getSubWallets(@NonNull String subUid) {
        return this.client.executeSync(this.api.getSubWallets(subUid));
    }

    @Override
    public void transfer(@NonNull String subUid, @NonNull String currency, @NonNull BigDecimal amount, @NonNull TransferTypeEnum type) {
        TransferParam param = TransferParam.builder()
                .subUid(subUid)
                .currency(currency)
                .amount(amount.stripTrailingZeros().toPlainString())
                .type(type.getType())
                .build();
        this.client.executeSync(this.api.transfer(param));
    }

    @Override
    public Address getDepositAddress(@NonNull String currency) {
        return this.client.executeSync(this.api.getDepositAddress(currency));
    }

    @Override
    public Page<Deposit> getDepositHistory(@NonNull String currency, boolean isDesc, int page, int size) {
        String direct = isDesc ? "next" : "prev";
        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 100;
        }
        return this.client.executeSync(this.api.getDepositHistory(currency, direct, page, size));
    }

}
