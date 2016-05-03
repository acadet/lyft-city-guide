package com.lyft.cityguide.services.google.distancematrix;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

import rx.Observable;

/**
 * IGoogleDistanceMatrixService
 * <p>
 */
public interface IGoogleDistanceMatrixService {
    /**
     * Mutates provided list
     *
     * @param currentLocation
     * @param pointOfInterests
     * @return
     */
    Observable<Void> fetchDistances(Location currentLocation, List<PointOfInterest> pointOfInterests);
}
