package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;

import java.util.List;

import rx.Observable;

/**
 * IGooglePlaceService
 * <p>
 */
public interface IGooglePlaceService {
    Observable<List<PointOfInterest>> search(Location currentLocation, SearchRangeSetting searchRangeSetting, PointOfInterest.Kind kind);

    Observable<List<PointOfInterest>> more();
}
