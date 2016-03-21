package com.lyft.cityguide.models.services.google.place;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.services.google.place.jobs.SearchPlacesJob;

import java.util.List;

import rx.Observable;

/**
 * GooglePlaceService
 * <p>
 */
class GooglePlaceService implements IGooglePlaceService {
    private SearchPlacesJob searchPlacesJob;

    GooglePlaceService(SearchPlacesJob searchPlacesJob) {
        this.searchPlacesJob = searchPlacesJob;
    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> search(Location currentLocation, float radiusInMeters, String type) {
        return searchPlacesJob.search(currentLocation, radiusInMeters, type);
    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> more() {
        return searchPlacesJob.more();
    }
}
