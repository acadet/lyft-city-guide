package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;

/**
 * IDataReadingBLL
 * <p>
 */
public interface IDataReadingBLL {
    Observable<List<PointOfInterestBLLDTO>> listPointOfInterestsAround(PlaceType placeType);

    Observable<List<PointOfInterestBLLDTO>> listMore();

    Observable<SearchRangeSettingBLLDTO> getSearchRangeSetting();
}
