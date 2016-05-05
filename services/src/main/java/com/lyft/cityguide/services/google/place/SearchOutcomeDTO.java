package com.lyft.cityguide.services.google.place;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchOutcomeDTO
 * <p>
 */
class SearchOutcomeDTO {
    private String         nextPageToken;
    private List<PlaceDTO> places;

    public SearchOutcomeDTO() {
        this.places = new ArrayList<>();
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public SearchOutcomeDTO addPlace(PlaceDTO place) {
        places.add(place);
        return this;
    }
}
