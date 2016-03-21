package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.bll.jobs.GetSearchRangeSettingJob;
import com.lyft.cityguide.models.bll.jobs.ListPointsOfInterestAroundJob;
import com.lyft.cityguide.models.bll.jobs.UpdateSearchRangeSettingJob;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * BLLFactory
 */
@Module
public class BLLFactory {
    @Provides
    @Singleton
    public IDataReadingBLL provideDataReading(
        ListPointsOfInterestAroundJob listPointsOfInterestAroundJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob) {
        return new DataReadingBLL(listPointsOfInterestAroundJob, getSearchRangeSettingJob);
    }

    @Provides
    @Singleton
    public IDataWritingBLL provideDataWriting(UpdateSearchRangeSettingJob updateSearchRangeSettingJob) {
        return new DataWritingBLL(updateSearchRangeSettingJob);
    }
}
