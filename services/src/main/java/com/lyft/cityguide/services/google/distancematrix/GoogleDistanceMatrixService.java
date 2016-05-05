package com.lyft.cityguide.services.google.distancematrix;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

import rx.Observable;

/**
 * GoogleDistanceMatrixService
 * <p>
 */
class GoogleDistanceMatrixService implements IGoogleDistanceMatrixService {

    private final FetchDistancesJob fetchDistancesJob;

    GoogleDistanceMatrixService(FetchDistancesJob fetchDistancesJob) {
        this.fetchDistancesJob = fetchDistancesJob;
    }

    @Override
    public Observable<Void> fetchDistances(Location currentLocation, List<PointOfInterest> pointOfInterests) {
        return fetchDistancesJob.create(currentLocation, pointOfInterests);
    }
}
