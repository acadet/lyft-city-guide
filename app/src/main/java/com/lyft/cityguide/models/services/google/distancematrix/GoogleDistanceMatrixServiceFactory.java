package com.lyft.cityguide.models.services.google.distancematrix;

import com.lyft.cityguide.models.services.google.distancematrix.jobs.FetchDistancesJob;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * GoogleDistanceMatrixServiceFactory
 * <p>
 */
@Module
public class GoogleDistanceMatrixServiceFactory {
    @Provides
    @Singleton
    public IGoogleDistanceMatrixService provideService(FetchDistancesJob fetchDistancesJob) {
        return new GoogleDistanceMatrixService(fetchDistancesJob);
    }
}
