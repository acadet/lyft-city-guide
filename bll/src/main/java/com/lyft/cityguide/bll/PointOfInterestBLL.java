package com.lyft.cityguide.bll;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

import rx.Observable;

/**
 * PointOfInterestBLL
 * <p>
 */
class PointOfInterestBLL implements IPointOfInterestBLL {
    private final ListPointsOfInterestAroundJob listPointsOfInterestAroundJob;
    private final GetSearchRangeSettingJob      getSearchRangeSettingJob;

    PointOfInterestBLL(
        ListPointsOfInterestAroundJob listPointsOfInterestAroundJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob) {
        this.listPointsOfInterestAroundJob = listPointsOfInterestAroundJob;
        this.getSearchRangeSettingJob = getSearchRangeSettingJob;
    }

    @Override
    public Observable<List<PointOfInterest>> listAround(PointOfInterest.Kind kind) {
        return listPointsOfInterestAroundJob.listAround(kind);
    }

    @Override
    public Observable<List<PointOfInterest>> listMore() {
        return listPointsOfInterestAroundJob.more();
    }
}
