package com.dd.api.websocket;

import com.dd.api.enums.SocketTopicEnum;

/**
 * @author zhangzp
 */
public interface WebSocket {
    /**
     * 连接服务器
     */
    void connect();

    /**
     * 关闭连接
     */
    void close();

    /**
     * 用户测试是否连接
     */
    void ping();

    /**
     * 订阅主题，
     *
     * @param topic 主题名：如 336_ENTRUST_ADD_ZT_USDT
     * @param size  返回数据大小，有些主题这个字段没用，如 {@link SocketTopicEnum#DEPTH},{@link SocketTopicEnum#TICKERS}
     * @see SocketTopicEnum
     */
    void subscribe(String topic, int size);

    /**
     * 取消订阅
     *
     * @param topic 主题名：如 336_ENTRUST_ADD_ZT_USDT
     * @see com.dd.api.enums.SocketTopicEnum
     */
    void unsubscribe(String topic);
}
