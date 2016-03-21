package com.lyft.cityguide.models.bll;

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
    public IDataWritingBLL provideDataWriting(UpdateSearchRangeSettingJob updateSearchRangeSettingJob) {
        return new DataWritingBLL(updateSearchRangeSettingJob);
    }
}
