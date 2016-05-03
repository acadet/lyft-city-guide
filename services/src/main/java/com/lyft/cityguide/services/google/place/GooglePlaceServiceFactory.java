package com.lyft.cityguide.services.google.place;

import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * GooglePlaceServiceFactory
 * <p>
 */
@Module
public class GooglePlaceServiceFactory {
    @Provides
    IGooglePlaceAPI provideAPI(Configuration configuration, SearchOutcomeDTOSerializer serializer) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.API_ENDPOINT)
            .setConverter(
                new GsonConverter(
                    new GsonBuilder()
                        .registerTypeAdapter(SearchOutcomeDTO.class, serializer)
                        .create()
                )
            )
            .build()
            .create(IGooglePlaceAPI.class);
    }

    @Provides
    @Singleton
    SearchOutcomeDTOSerializer provideSearchOutcomeDTOSerializer() {
        return new SearchOutcomeDTOSerializer();
    }

    @Provides
    @Singleton
    SearchPlacesJob provideSearchPlacesJob(Configuration configuration, IGooglePlaceAPI api, IPointOfInterestMapper mapper) {
        return new SearchPlacesJob(configuration, api, mapper);
    }

    @Provides
    @Singleton
    public IGooglePlaceService provideService(SearchPlacesJob searchPlacesJob) {
        return new GooglePlaceService(searchPlacesJob);
    }
}
