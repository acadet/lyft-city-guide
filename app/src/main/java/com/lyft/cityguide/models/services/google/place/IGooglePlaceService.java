package com.lyft.cityguide.models.services.google.place;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;

/**
 * IGooglePlaceService
 * <p>
 */
public interface IGooglePlaceService {
    Observable<List<PointOfInterestBLLDTO>> search(Location currentLocation, SearchRangeSettingBLLDTO searchRangeSetting, PlaceType type);

    Observable<List<PointOfInterestBLLDTO>> more();
}
