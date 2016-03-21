package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.models.bll.jobs.UpdateSearchRangeSettingJob;

import rx.Observable;

/**
 * DataWritingBLL
 * <p>
 */
class DataWritingBLL implements IDataWritingBLL {
    private UpdateSearchRangeSettingJob updateSearchRangeSettingJob;

    DataWritingBLL(UpdateSearchRangeSettingJob updateSearchRangeSettingJob) {
        this.updateSearchRangeSettingJob = updateSearchRangeSettingJob;
    }

    @Override
    public Observable<Void> updateSearchRangeSetting(SearchRangeSettingBLLDTO setting) {
        return updateSearchRangeSettingJob.get(setting);
    }
}
