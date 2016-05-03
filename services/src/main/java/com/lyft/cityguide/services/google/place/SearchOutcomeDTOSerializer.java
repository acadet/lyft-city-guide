package com.lyft.cityguide.services.google.place;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * SearchOutcomeDTOSerializer
 * <p>
 */
class SearchOutcomeDTOSerializer implements JsonDeserializer<SearchOutcomeDTO> {
    @Override
    public SearchOutcomeDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SearchOutcomeDTO outcome = new SearchOutcomeDTO();
        JsonArray resultNode;

        if (json.getAsJsonObject().has("next_page_token")) {
            outcome.setNextPageToken(json.getAsJsonObject().get("next_page_token").getAsString());
        }

        resultNode = json.getAsJsonObject().get("results").getAsJsonArray();
        for (JsonElement e : resultNode) {
            JsonObject o = e.getAsJsonObject();
            JsonObject locationNode;
            PlaceDTO p = new PlaceDTO();

            p.setId(o.get("id").getAsString());
            p.setName(o.get("name").getAsString());
            p.setRating(o.get("rating").getAsFloat());

            locationNode = o.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
            p.setLatitude(locationNode.get("lat").getAsFloat());
            p.setLongitude(locationNode.get("lng").getAsFloat());

            outcome.addPlace(p);
        }

        return outcome;
    }
}
