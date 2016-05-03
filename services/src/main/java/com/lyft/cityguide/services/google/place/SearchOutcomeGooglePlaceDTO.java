package com.lyft.cityguide.services.google.place;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchOutcomeGooglePlaceDTO
 * <p>
 */
class SearchOutcomeGooglePlaceDTO {
    private String                    nextPageToken;
    private List<PlaceGooglePlaceDTO> places;

    public SearchOutcomeGooglePlaceDTO() {
        this.places = new ArrayList<>();
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public boolean hasNextPageToken() {
        return nextPageToken != null;
    }

    public List<PlaceGooglePlaceDTO> getPlaces() {
        return places;
    }

    public SearchOutcomeGooglePlaceDTO addPlace(PlaceGooglePlaceDTO place) {
        places.add(place);
        return this;
    }

    public boolean hasPlaces() {
        return places.size() > 0;
    }
}
