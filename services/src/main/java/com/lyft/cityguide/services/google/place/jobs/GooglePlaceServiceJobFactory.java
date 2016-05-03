package com.lyft.cityguide.services.google.place.jobs;

import com.lyft.cityguide.SecretApplicationConfiguration;
import com.lyft.cityguide.models.bll.serializers.PointOfInterestBLLDTOSerializer;
import com.lyft.cityguide.services.google.place.api.IGooglePlaceAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * GooglePlaceServiceJobFactory
 * <p>
 */
@Module
public class GooglePlaceServiceJobFactory {
    @Provides
    @Singleton
    public SearchPlacesJob provideJob(SecretApplicationConfiguration configuration, IGooglePlaceAPI api, PointOfInterestBLLDTOSerializer serializer) {
        return new SearchPlacesJob(configuration, api, serializer);
    }
}
