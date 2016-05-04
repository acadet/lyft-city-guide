package com.lyft.cityguide.bll;

import com.lyft.cityguide.domain.SearchRangeSetting;

import rx.Observable;

/**
 * SearchSettingBLL
 * <p>
 */
class SearchSettingBLL implements ISearchSettingBLL {
    private final UpdateSearchRangeSettingJob updateSearchRangeSettingJob;
    private final GetSearchRangeSettingJob    getSearchRangeSettingJob;

    SearchSettingBLL(UpdateSearchRangeSettingJob updateSearchRangeSettingJob,
                     GetSearchRangeSettingJob getSearchRangeSettingJob) {
        this.updateSearchRangeSettingJob = updateSearchRangeSettingJob;
        this.getSearchRangeSettingJob = getSearchRangeSettingJob;
    }


    @Override
    public Observable<Void> update(SearchRangeSetting newValue) {
        return updateSearchRangeSettingJob.create(newValue);
    }

    @Override
    public Observable<SearchRangeSetting> get() {
        return getSearchRangeSettingJob.create();
    }
}
