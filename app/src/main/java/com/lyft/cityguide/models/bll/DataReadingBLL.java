package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.models.bll.jobs.GetSearchRangeSettingJob;
import com.lyft.cityguide.models.bll.jobs.ListPointsOfInterestAroundJob;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;

/**
 * DataReadingBLL
 * <p>
 */
class DataReadingBLL implements IDataReadingBLL {
    private ListPointsOfInterestAroundJob listPointsOfInterestAroundJob;
    private GetSearchRangeSettingJob      getSearchRangeSettingJob;

    private PlaceType latestPlaceType;

    DataReadingBLL(
        ListPointsOfInterestAroundJob listPointsOfInterestAroundJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob) {
        this.listPointsOfInterestAroundJob = listPointsOfInterestAroundJob;
        this.getSearchRangeSettingJob = getSearchRangeSettingJob;
    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> listPointOfInterestsAround(PlaceType placeType) {
        latestPlaceType = placeType;
        return listPointsOfInterestAroundJob.get(false, latestPlaceType);
    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> listMore() {
        return listPointsOfInterestAroundJob.get(true, latestPlaceType);
    }

    @Override
    public Observable<SearchRangeSettingBLLDTO> getSearchRangeSetting() {
        return getSearchRangeSettingJob.get();
    }
}
