package com.dd.api.service.account;

import com.dd.api.config.ApiConfig;
import com.dd.api.entity.account.result.*;
import com.dd.api.entity.commom.Page;
import com.dd.api.enums.WithdrawStateEnum;
import com.dd.api.service.account.impl.WithdrawServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * @author zhangzp
 */
public class WithdrawServiceTest {
    private WithdrawService withdrawService;

    @Before
    public void setUp() throws Exception {
        ApiConfig config = ApiConfig.builder()
                .endpoint("https://www.zbg.com")
                .apiKey("7sNIR1ngJ9c7sNIR1ngJ9d")
                .secretKey("867f185e869553b1e8692557959d5e88")
                .passphrase("123654")
                .print(true)
                .build();
        this.withdrawService = new WithdrawServiceImpl(config);
    }

    @Test
    public void getWithdrawAddress() {
        List<Address> result = withdrawService.getWithdrawAddress("qc");
        System.out.println(result);
    }

    @Test
    public void withdraw() {
//        String withdrawId = accountService.withdraw("qc", "QLcwMMTawEsED3KDb9bE6b1g5inenBGGpZ", new BigDecimal("100"));
        String cancelWithdraw = withdrawService.cancelWithdraw("W6611243024830066688");
//        assertEquals(withdrawId, cancelWithdraw);
    }

    @Test
    public void cancelWithdraw() {
    }

    @Test
    public void getWithdrawHistory() {
        Page<Withdraw> result = withdrawService.getWithdrawHistory("qc", true, 1, 10);
        WithdrawStateEnum state = result.getList().get(0).getState();
        System.out.println(state);
        System.out.println(result);
    }
}