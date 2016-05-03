package com.lyft.cityguide.services.google.distancematrix.jobs;

import com.lyft.cityguide.SecretApplicationConfiguration;
import com.lyft.cityguide.services.google.distancematrix.api.IGoogleDistanceMatrixAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * GoogleDistanceMatrixServiceJobFactory
 * <p>
 */
@Module
public class GoogleDistanceMatrixServiceJobFactory {
    @Provides
    @Singleton
    public FetchDistancesJob provideFetchDistancesJob(SecretApplicationConfiguration configuration, IGoogleDistanceMatrixAPI api) {
        return new FetchDistancesJob(configuration, api);
    }
}
