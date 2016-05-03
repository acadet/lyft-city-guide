package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.services.google.place.jobs.SearchPlacesJob;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;
import timber.log.Timber;

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
    public Observable<List<PointOfInterestBLLDTO>> search(Location currentLocation, SearchRangeSettingBLLDTO searchRangeSetting, PlaceType type) {
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

        switch (type) {
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
    public Observable<List<PointOfInterestBLLDTO>> more() {
        return searchPlacesJob.more();
    }
}
