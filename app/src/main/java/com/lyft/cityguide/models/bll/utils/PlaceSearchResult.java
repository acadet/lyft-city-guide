package com.lyft.cityguide.models.bll.utils;

import com.lyft.cityguide.models.beans.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * @class PlaceSearchResult
 * @brief
 */
public class PlaceSearchResult {
    private List<Place> _results;
    private String      _pageToken;

    public PlaceSearchResult() {
        _results = new ArrayList<>();
    }

    public List<Place> getResults() {
        return _results;
    }

    public PlaceSearchResult addResult(Place place) {
        _results.add(place);
        return this;
    }

    public String getPageToken() {
        return _pageToken;
    }

    public void setPageToken(String value) {
        _pageToken = value;
    }
}
