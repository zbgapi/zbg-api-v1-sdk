package com.dd.api.websocket.subscribe.event;

import com.dd.api.entity.commom.result.Ticker;
import com.dd.api.entity.commom.result.Trade;
import com.dd.api.websocket.subscribe.SubscribeEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class TickerSubscribeEvent implements SubscribeEvent<TickerSubscribeEvent> {
    /**
     * 数据类型
     */
    private String dataType = "S";
    /**
     * 返回数据条数
     */
    private int dataSize;
    /**
     * 数据
     */
    private List<Ticker> data = new ArrayList<>();

    @Override
    public TickerSubscribeEvent parse(String message) {
        TickerSubscribeEvent event = new TickerSubscribeEvent();

        Gson gson = new Gson();
        JsonObject object = gson.fromJson(message, JsonObject.class);

        JsonArray array = object.getAsJsonArray("trade_statistic");
        for (JsonElement element : array) {
            JsonArray jsonArray = element.getAsJsonArray();
            event.addData(Ticker.valueOf(jsonArray));
        }

        return event;
    }

    private void addData(Ticker ticker) {
        data.add(ticker);
        dataSize++;
    }
}
