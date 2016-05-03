package com.lyft.cityguide.services.google.distancematrix;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;

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
    Observable<Void> fetchDistances(Location currentLocation, List<PointOfInterestBLLDTO> pointOfInterests);
}
