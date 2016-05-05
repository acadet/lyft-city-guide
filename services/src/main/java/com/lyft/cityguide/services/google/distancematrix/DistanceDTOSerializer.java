package com.lyft.cityguide.services.google.distancematrix;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * DistanceDTOSerializer
 * <p>
 * Cf. documentation for schema
 */
class DistanceDTOSerializer implements JsonDeserializer<DistanceDTO> {
    @Override
    public DistanceDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DistanceDTO result = new DistanceDTO();
        JsonArray rowNode, elementNode;

        rowNode = json.getAsJsonObject()
                      .get("rows")
                      .getAsJsonArray();

        if (rowNode.size() < 1) { // No result has been returned
            return result;
        }

        elementNode = rowNode
            .get(0)
            .getAsJsonObject()
            .get("elements")
            .getAsJsonArray();

        for (JsonElement e : elementNode) {
            if (e.getAsJsonObject().has("distance")) {
                float value = e
                    .getAsJsonObject()
                    .get("distance")
                    .getAsJsonObject()
                    .get("value")
                    .getAsFloat();

                result.addDistance(value);
            }
        }

        return result;
    }
}
