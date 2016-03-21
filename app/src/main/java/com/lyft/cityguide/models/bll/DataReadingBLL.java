package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.models.bll.jobs.GetSearchRangeSettingJob;

import java.util.List;

import rx.Observable;

/**
 * DataReadingBLL
 * <p>
 */
class DataReadingBLL implements IDataReadingBLL {
    private GetSearchRangeSettingJob getSearchRangeSettingJob;

    DataReadingBLL(
        GetSearchRangeSettingJob getSearchRangeSettingJob) {
        this.getSearchRangeSettingJob = getSearchRangeSettingJob;
    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> listPointOfInterestsAround() {

    }

    @Override
    public Observable<List<PointOfInterestBLLDTO>> listMore() {
        return null;
    }

    @Override
    public Observable<SearchRangeSettingBLLDTO> getSearchRangeSetting() {
        return getSearchRangeSettingJob.get();
    }
}
