package com.dd.api.websocket.subscribe.event;

import com.dd.api.entity.commom.result.Kline;
import com.dd.api.websocket.subscribe.SubscribeEvent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzp
 */
@Data
public class OrderChangeSubscribeEvent implements SubscribeEvent<OrderChangeSubscribeEvent> {
    /**
     * 数据类型
     */
    private String dataType = "R";
    /**
     * 返回数据时服务器时间戳，秒
     */
    private long timestamp;
    /**
     * 返回数据条数
     */
    private int dataSize;
    /**
     * 数据
     */
    private List<OrderChange> data = new ArrayList<>();

    @Override
    public OrderChangeSubscribeEvent parse(String message) {
        OrderChangeSubscribeEvent event = new OrderChangeSubscribeEvent();

        Gson gson = new Gson();
        JsonArray array = gson.fromJson(message, JsonArray.class);
        if (array.get(0).isJsonArray()) {
            JsonArray data = array.get(0).getAsJsonArray();
            event.setTimestamp(data.get(3).getAsLong());
            array = data.get(4).getAsJsonArray();
            for (JsonElement element : array) {
                JsonArray jsonArray = element.getAsJsonArray();
                event.addData(OrderChange.valueOf(jsonArray));
            }
        } else {
            event.setTimestamp(array.get(3).getAsLong());

            array.remove(0);
            array.remove(0);
            array.remove(0);
            array.remove(0);
            event.addData(OrderChange.valueOf(array));
        }

        return event;
    }

    public void addData(OrderChange change) {
        this.data.add(change);
        this.dataSize++;
    }
}
