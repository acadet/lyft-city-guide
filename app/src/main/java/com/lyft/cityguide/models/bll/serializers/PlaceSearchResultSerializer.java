package com.lyft.cityguide.models.bll.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.models.bll.utils.PlaceSearchResult;

import java.lang.reflect.Type;

/**
 * @class PlaceSearchResultSerializer
 * @brief
 */
public class PlaceSearchResultSerializer implements JsonDeserializer<PlaceSearchResult> {
    @Override
    public PlaceSearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PlaceSearchResult results = new PlaceSearchResult();
        JsonArray resultNode;

        results.setPageToken(json.getAsJsonObject().get("next_page_token").getAsString());
        resultNode = json.getAsJsonObject().get("results").getAsJsonArray();
        for (JsonElement e : resultNode) {
            JsonObject o = e.getAsJsonObject();
            JsonObject locationNode;
            Place p = new Place();

            p.setId(o.get("id").getAsString());
            p.setName(o.get("name").getAsString());

            locationNode = o.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
            p.setLatitude(locationNode.get("lat").getAsFloat());
            p.setLongitude(locationNode.get("lng").getAsFloat());

            results.addResult(p);
        }

        return results;
    }
}
