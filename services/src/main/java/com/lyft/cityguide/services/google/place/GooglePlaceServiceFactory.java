package com.lyft.cityguide.services.google.place;

import com.lyft.cityguide.services.google.place.jobs.SearchPlacesJob;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * GooglePlaceServiceFactory
 * <p>
 */
@Module
public class GooglePlaceServiceFactory {
    @Provides
    @Singleton
    public IGooglePlaceService provideService(SearchPlacesJob searchPlacesJob) {
        return new GooglePlaceService(searchPlacesJob);
    }
}
