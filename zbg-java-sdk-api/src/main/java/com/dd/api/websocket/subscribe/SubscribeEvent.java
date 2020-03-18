package com.dd.api.websocket.subscribe;

/**
 * 订阅事件数据
 *
 * @author zhangzp
 */
public interface SubscribeEvent<T extends SubscribeEvent> {

    /**
     * 将推送的数据转换事件数据对象
     *
     * @param message 推送数据
     * @return 订阅数据
     */
    T parse(String message);
}
