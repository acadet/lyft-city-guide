package com.lyft.cityguide.bll;

import android.content.Context;
import android.location.LocationManager;

import com.lyft.cityguide.dao.ISettingsDAO;
import com.lyft.cityguide.services.google.distancematrix.IGoogleDistanceMatrixService;
import com.lyft.cityguide.services.google.place.IGooglePlaceService;

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
    LocationManager provideLocationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    @Singleton
    UpdateSearchRangeSettingJob provideUpdateSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        return new UpdateSearchRangeSettingJob(settingsDAO);
    }

    @Provides
    @Singleton
    GetSearchRangeSettingJob provideGetSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        return new GetSearchRangeSettingJob(settingsDAO);
    }

    @Provides
    @Singleton
    GetCurrentLocationJob provideGetCurrentLocationJob(Configuration configuration, LocationManager locationManager) {
        return new GetCurrentLocationJob(configuration, locationManager);
    }

    @Provides
    @Singleton
    ListPointsOfInterestJob provideListPointsOfInterestJob(IGooglePlaceService googlePlaceService, IGoogleDistanceMatrixService distanceMatrixService) {
        return new ListPointsOfInterestJob(googlePlaceService, distanceMatrixService);
    }

    @Provides
    @Singleton
    ListPointsOfInterestAroundJob provideListPointsOfInterestAroundJob(
        GetCurrentLocationJob getCurrentLocationJob,
        ListPointsOfInterestJob listPointsOfInterestJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob
    ) {
        return new ListPointsOfInterestAroundJob(getCurrentLocationJob, listPointsOfInterestJob, getSearchRangeSettingJob);
    }

    @Provides
    @Singleton
    public IPointOfInterestBLL providePointOfInterestBLL(
        ListPointsOfInterestAroundJob listPointsOfInterestAroundJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob) {
        return new PointOfInterestBLL(listPointsOfInterestAroundJob, getSearchRangeSettingJob);
    }

    @Provides
    @Singleton
    public ISearchSettingBLL provideSearchSettingBLL(UpdateSearchRangeSettingJob updateSearchRangeSettingJob, GetSearchRangeSettingJob getSearchRangeSettingJob) {
        return new SearchSettingBLL(updateSearchRangeSettingJob, getSearchRangeSettingJob);
    }
}
