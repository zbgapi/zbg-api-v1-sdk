package com.dd.api.service.common;

import com.dd.api.entity.commom.result.AssistPrice;
import com.dd.api.entity.commom.result.Currency;
import com.dd.api.entity.commom.result.ServerTime;
import com.dd.api.entity.commom.result.Symbol;

import java.util.List;

/**
 * @author zhangzp
 */
public interface CommonApiService {

    /**
     * 获取所有币种
     *
     * @return 币种列表
     */
    List<Currency> getCurrencies();

    /**
     * 获取所有交易对
     *
     * @return 交易对列表
     */
    List<Symbol> getSymbols();

    /**
     * 根据交易对名称获取交易对
     *
     * @param name 交易对名称，忽略大小写
     * @return 交易对对象
     */
    Symbol getSymbol(String name);

    /**
     * 获取服务器的时间
     *
     * @return 服务器的时间
     */
    ServerTime getServerTime();

    /**
     * 获取服务器币种对 cny，btc，usd的辅助价格
     *
     * @return 辅助价格
     */
    AssistPrice getAssistPrice(String... currencies);
}
