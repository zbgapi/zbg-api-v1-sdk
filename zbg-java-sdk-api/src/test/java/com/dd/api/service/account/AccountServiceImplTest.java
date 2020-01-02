package com.dd.api.service.account;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.account.result.*;
import com.dd.api.entity.commom.Page;
import com.dd.api.enums.TransferTypeEnum;
import com.dd.api.service.account.impl.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author zhangzp
 */
public class AccountServiceImplTest {
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        ApiConfig config = ApiConfig.builder()
                .endpoint("https://www.zbgpro.net")
                .apiKey("7sNIR1ngJ9c7sNIR1ngJ9d")
                .secretKey("867f185e869553b1e8692557959d5e88")
                .passphrase("123654")
                .print(true)
                .build();
        this.accountService = new AccountServiceImpl(config);
    }


    @Test
    public void getAccount() {
        Account result = accountService.getAccount();
        assertNotNull(result);
    }

    @Test
    public void getWallets() {
        List<Wallet> result = accountService.getWallets();
        assertNotNull(result);
    }

    @Test
    public void getWallet() {
        Wallet result = accountService.getWallet("usdt");
        assertNotNull(result);
    }

    @Test
    public void getAggregateWallets() {
        List<Wallet> result = accountService.getAggregateWallets();
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void getSubWallets() {
        List<Wallet> result = accountService.getSubWallets("7nUklLOskt6");
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    public void transfer() {
        accountService.transfer("7nUklLOskt6", "zt", new BigDecimal(1), TransferTypeEnum.MASTER_TRANSFER_OUT);
        List<Wallet> result = accountService.getSubWallets("7nUklLOskt6");
        System.out.println(result);
        accountService.transfer("7nUklLOskt6", "zt", new BigDecimal(1), TransferTypeEnum.MASTER_TRANSFER_IN);
    }

    @Test
    public void getDepositAddress() {
        Address result = accountService.getDepositAddress("btc");
        System.out.println(result);

        result = accountService.getDepositAddress("eos");
        System.out.println(result);
        assertNotNull(result.getMemo());
    }

    @Test
    public void getDepositHistory() {
        Page<Deposit> result = accountService.getDepositHistory("zt", true, 1, 10);
        System.out.println(result);
    }
}