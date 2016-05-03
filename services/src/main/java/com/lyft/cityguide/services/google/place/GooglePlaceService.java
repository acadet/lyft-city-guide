package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;

import java.util.List;

import rx.Observable;
import timber.log.Timber;

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
        float radiusInMeters;
        String stringifyType;

        switch (searchRangeSetting) {
            case ONE_MILE:
                radiusInMeters = 1;
                break;
            case TWO_MILE:
                radiusInMeters = 2;
                break;
            case FIVE_MILE:
                radiusInMeters = 5;
                break;
            default:
                radiusInMeters = 1;
                Timber.e("Unexpected search range setting");
                break;
        }

        radiusInMeters *= 1609;

        switch (kind) {
            case BAR:
                stringifyType = "bar";
                break;
            case BISTRO:
                stringifyType = "restaurant";
                break;
            case CAFE:
                stringifyType = "cafe";
                break;
            default:
                stringifyType = "bar";
                Timber.e("Unexpected type");
                break;
        }

        return searchPlacesJob.search(currentLocation, radiusInMeters, stringifyType);
    }

    @Override
    public Observable<List<PointOfInterest>> more() {
        return searchPlacesJob.more();
    }
}
