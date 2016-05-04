package com.lyft.cityguide.models.bll.jobs;

import android.content.Context;

import com.lyft.cityguide.ApplicationConfiguration;
import com.lyft.cityguide.dao.ISettingsDAO;
import com.lyft.cityguide.services.google.distancematrix.IGoogleDistanceMatrixService;
import com.lyft.cityguide.services.google.place.IGooglePlaceService;

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

    @Provides
    @Singleton
    public GetCurrentLocationJob provideGetCurrentLocationJob(ApplicationConfiguration configuration, Context context) {
        return new GetCurrentLocationJob(configuration, context);
    }

    @Provides
    @Singleton
    public ListPointsOfInterestJob provideListPointsOfInterestJob(IGooglePlaceService googlePlaceService, IGoogleDistanceMatrixService distanceMatrixService) {
        return new ListPointsOfInterestJob(googlePlaceService, distanceMatrixService);
    }

    @Provides
    @Singleton
    public ListPointsOfInterestAroundJob provideListPointsOfInterestAroundJob(
        GetCurrentLocationJob getCurrentLocationJob,
        ListPointsOfInterestJob listPointsOfInterestJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob
    ) {
        return new ListPointsOfInterestAroundJob(getCurrentLocationJob, listPointsOfInterestJob, getSearchRangeSettingJob);
    }
}
