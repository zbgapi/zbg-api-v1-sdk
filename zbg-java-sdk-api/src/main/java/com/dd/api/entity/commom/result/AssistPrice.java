package com.dd.api.entity.commom.result;

import lombok.Data;

import java.util.Map;

/**
 * @author zhangzp
 */
@Data
public class AssistPrice {
    /**
     * 币种对cny的折算价格，key 是币种名称，value 辅助价格
     */
    private Map<String, String> cny;

    /**
     * 币种对btc的折算价格，key 是币种名称，value 辅助价格
     */
    private Map<String, String> btc;

    /**
     * 币种对usd的折算价格，key 是币种名称，value 辅助价格
     */
    private Map<String, String> usd;
}
