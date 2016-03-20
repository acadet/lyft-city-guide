package com.lyft.cityguide.models.services.google.place.api;

import com.google.gson.GsonBuilder;
import com.lyft.cityguide.ApplicationConfiguration;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * GooglePlaceAPIFactory
 * <p>
 */
@Module
public class GooglePlaceAPIFactory {
    @Provides
    public IGooglePlaceAPI provideJSONAPI(ApplicationConfiguration configuration) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.GOOGLE_PLACE_API_ENDPOINT)
            .setConverter(new GsonConverter(new GsonBuilder().create()))
            .build()
            .create(IGooglePlaceAPI.class);
    }
}
