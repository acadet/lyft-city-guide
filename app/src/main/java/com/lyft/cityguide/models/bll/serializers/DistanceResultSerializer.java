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
 * @brief Cf. documentation for schema
 */
public class DistanceResultSerializer implements JsonDeserializer<DistanceResult> {
    @Override
    public DistanceResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DistanceResult result = new DistanceResult();
        JsonArray rowNode, elementNode;

        rowNode = json.getAsJsonObject()
                      .get("rows")
                      .getAsJsonArray();

        if (rowNode.size() < 1) { // No result has been returned
            return result;
        }

        elementNode = rowNode.get(0)
                             .getAsJsonObject()
                             .get("elements")
                             .getAsJsonArray();

        for (JsonElement e : elementNode) {
            if (e.getAsJsonObject().has("distance")) {
                Distance d = new Distance();
                float value = e.getAsJsonObject()
                               .get("distance")
                               .getAsJsonObject()
                               .get("value")
                               .getAsFloat();

                d.setDistance(value);
                result.addDistance(d);
            }
        }

        return result;
    }
}
