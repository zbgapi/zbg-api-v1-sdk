package com.dd.api.websocket.subscribe.event;

import com.dd.api.entity.commom.result.Kline;
import com.dd.api.entity.commom.result.Trade;
import com.dd.api.websocket.subscribe.SubscribeEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class TradeSubscribeEvent implements SubscribeEvent<TradeSubscribeEvent> {
    /**
     * 数据类型
     */
    private String dataType = "T";
    /**
     * 返回数据条数
     */
    private int dataSize;
    /**
     * 数据
     */
    private List<Trade> data = new ArrayList<>();

    @Override
    public TradeSubscribeEvent parse(String message) {
        TradeSubscribeEvent event = new TradeSubscribeEvent();

        Gson gson = new Gson();
        JsonArray array = gson.fromJson(message, JsonArray.class);
        if (array.get(0).isJsonArray()) {
            for (JsonElement element : array) {
                JsonArray jsonArray = element.getAsJsonArray();
                event.addData(Trade.valueOf(jsonArray));
            }
        } else {
            event.addData(Trade.valueOf(array));
        }

        return event;
    }

    private void addData(Trade trade) {
        data.add(trade);
        dataSize++;
    }
}
