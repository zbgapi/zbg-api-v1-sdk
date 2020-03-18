package com.dd.api.websocket.subscribe.event;

import com.dd.api.entity.commom.result.Kline;
import com.dd.api.websocket.subscribe.SubscribeEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzp
 */
@ToString
public class KlineSubscribeEvent implements SubscribeEvent<KlineSubscribeEvent> {
    /**
     * 数据类型
     */
    private String dataType = "K";
    /**
     * 返回数据条数
     */
    private int dataSize;
    /**
     * 数据
     */
    private List<Kline> data = new ArrayList<>();

    @Override
    public KlineSubscribeEvent parse(String message) {
        KlineSubscribeEvent event = new KlineSubscribeEvent();

        Gson gson = new Gson();
        JsonArray array = gson.fromJson(message, JsonArray.class);
        if (array.get(0).isJsonArray()) {
            for (JsonElement element : array) {
                JsonArray jsonArray = element.getAsJsonArray();
                event.addData(Kline.valueOf(jsonArray));
            }
        } else {
            event.addData(Kline.valueOf(array));
        }

        return event;
    }

    public String getDataType() {
        return dataType;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public List<Kline> getData() {
        return data;
    }

    public void setData(List<Kline> data) {
        this.data = data;
        this.dataSize = data.size();
    }

    public void addData(Kline kline) {
        this.data.add(kline);
        this.dataSize++;
    }
}
