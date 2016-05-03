package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;

import java.util.List;

import rx.Observable;

/**
 * GooglePlaceService
 * <p>
 */
class GooglePlaceService implements IGooglePlaceService {
    private final SearchPlacesJob searchPlacesJob;

    GooglePlaceService(SearchPlacesJob searchPlacesJob) {
        this.searchPlacesJob = searchPlacesJob;
    }

    @Override
    public Observable<List<PointOfInterest>> search(Location currentLocation, SearchRangeSetting searchRangeSetting, PointOfInterest.Kind kind) {
        return searchPlacesJob.search(currentLocation, searchRangeSetting, kind);
    }

    @Override
    public Observable<List<PointOfInterest>> more() {
        return searchPlacesJob.more();
    }
}
