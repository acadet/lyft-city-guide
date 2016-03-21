package com.lyft.cityguide.models.services.google.distancematrix.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.lyft.cityguide.models.services.google.distancematrix.dto.DistanceGoogleDistanceMatrixDTO;

import java.lang.reflect.Type;

/**
 * DistanceGoogleDistanceMatrixDTOSerializer
 * <p>
 * Cf. documentation for schema
 */
class DistanceGoogleDistanceMatrixDTOSerializer implements JsonDeserializer<DistanceGoogleDistanceMatrixDTO> {
    @Override
    public DistanceGoogleDistanceMatrixDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        DistanceGoogleDistanceMatrixDTO result = new DistanceGoogleDistanceMatrixDTO();
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
