package com.lyft.cityguide.models.services.google.place;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;

import java.util.List;

import rx.Observable;

/**
 * IGooglePlaceService
 * <p>
 */
public interface IGooglePlaceService {
    Observable<List<PointOfInterestBLLDTO>> search(Location currentLocation, float radiusInMeters, String type);

    Observable<List<PointOfInterestBLLDTO>> more();
}
