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
    IGooglePlaceAPI provideJSONAPI(ApplicationConfiguration configuration, SearchOutcomeGooglePlaceDTOSerializer serializer) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.GOOGLE_PLACE_API_ENDPOINT)
            .setConverter(
                new GsonConverter(
                    new GsonBuilder()
                        .registerTypeAdapter(SearchOutcomeGooglePlaceDTO.class, serializer)
                        .create()
                )
            )
            .build()
            .create(IGooglePlaceAPI.class);
    }

    @Provides
    @Singleton
    SearchOutcomeGooglePlaceDTOSerializer provideSerializer() {
        return new SearchOutcomeGooglePlaceDTOSerializer();
    }

    @Provides
    @Singleton
    SearchPlacesJob provideJob(SecretApplicationConfiguration configuration, IGooglePlaceAPI api, PointOfInterestBLLDTOSerializer serializer) {
        return new SearchPlacesJob(configuration, api, serializer);
    }

    @Provides
    @Singleton
    public IGooglePlaceService provideService(SearchPlacesJob searchPlacesJob) {
        return new GooglePlaceService(searchPlacesJob);
    }
}
