package com.dd.api.websocket.subscribe.event;

import com.dd.api.entity.commom.result.DepthEntry;
import com.dd.api.entity.commom.result.PriceDepth;
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
public class PriceDepthSubscribeEvent implements SubscribeEvent<PriceDepthSubscribeEvent> {
    /**
     * 数据类型
     */
    private String dataType = "AE";
    /**
     * 市场深度，首次查全量时有值
     */
    private PriceDepth priceDepth;
    /**
     * 增量数据
     */
    private PriceDepthItem item;

    @Override
    public PriceDepthSubscribeEvent parse(String message) {
        PriceDepthSubscribeEvent event = new PriceDepthSubscribeEvent();

        Gson gson = new Gson();
        JsonArray array = gson.fromJson(message, JsonArray.class);
        if (array.get(0).isJsonArray()) {
            JsonArray jsonArray = array.get(0).getAsJsonArray();
            PriceDepth priceDepth = new PriceDepth();
            priceDepth.setSymbol(jsonArray.get(2).getAsString());
            priceDepth.setTimestamp(jsonArray.get(3).getAsLong());

            JsonArray asks = jsonArray.get(4).getAsJsonObject().getAsJsonArray("asks");
            JsonArray bids = jsonArray.get(5).getAsJsonObject().getAsJsonArray("bids");

            priceDepth.setAsks(parseDepthEntryList(asks));
            priceDepth.setBids(parseDepthEntryList(bids));

            event.setPriceDepth(priceDepth);
        } else {
            event.setDataType("E");
            event.setItem(PriceDepthItem.valueOf(array));
        }

        return event;
    }

    private List<DepthEntry> parseDepthEntryList(JsonArray list) {
        List<DepthEntry> depthEntries = new ArrayList<>();
        for (JsonElement element : list) {
            DepthEntry entry = new DepthEntry();
            entry.setPrice(element.getAsJsonArray().get(0).getAsBigDecimal());
            entry.setAmount(element.getAsJsonArray().get(1).getAsBigDecimal());
            depthEntries.add(entry);
        }

        return depthEntries;
    }

    public boolean isAllItem() {
        return "AE".equals(dataType);
    }
}
