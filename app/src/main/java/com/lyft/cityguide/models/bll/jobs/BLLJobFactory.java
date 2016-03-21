package com.lyft.cityguide.models.bll.jobs;

import com.lyft.cityguide.models.dao.ISettingsDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * BLLJobFactory
 * <p>
 */
@Module
public class BLLJobFactory {
    @Provides
    @Singleton
    public UpdateSearchRangeSettingJob provideUpdateSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        return new UpdateSearchRangeSettingJob(settingsDAO);
    }

    @Provides
    @Singleton
    public GetSearchRangeSettingJob provideGetSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        return new GetSearchRangeSettingJob(settingsDAO);
    }
}
