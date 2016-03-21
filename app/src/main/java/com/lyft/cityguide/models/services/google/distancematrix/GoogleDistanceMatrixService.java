package com.lyft.cityguide.models.services.google.distancematrix;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.services.google.distancematrix.jobs.FetchDistancesJob;

import java.util.List;

import rx.Observable;

/**
 * GoogleDistanceMatrixService
 * <p>
 */
class GoogleDistanceMatrixService implements IGoogleDistanceMatrixService {

    private FetchDistancesJob fetchDistancesJob;

    GoogleDistanceMatrixService(FetchDistancesJob fetchDistancesJob) {
        this.fetchDistancesJob = fetchDistancesJob;
    }

    @Override
    public Observable<Void> fetchDistances(Location currentLocation, List<PointOfInterestBLLDTO> pointOfInterests) {
        return fetchDistancesJob.get(currentLocation, pointOfInterests);
    }
}
