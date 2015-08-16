package com.lyft.cityguide.models.bll.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lyft.cityguide.models.beans.Distance;
import com.lyft.cityguide.models.bll.structs.DistanceResult;

import java.lang.reflect.Type;

/**
 * @class DistanceResultSerializer
 * @brief
 */
public class DistanceResultSerializer implements JsonDeserializer<DistanceResult> {
    @Override
    public DistanceResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DistanceResult result = new DistanceResult();
        JsonArray rowNode;

        rowNode = json.getAsJsonObject().get("rows").getAsJsonArray();
        for (JsonElement e : rowNode) {
            Distance d = new Distance();
            float value = e
                .getAsJsonObject()
                .get("elements")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("distance")
                .getAsJsonObject()
                .get("value")
                .getAsFloat();

            d.setDistance(value);
            result.addDistance(d);
        }

        return result;
    }
}
