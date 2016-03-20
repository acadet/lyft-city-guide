package com.lyft.cityguide.models.services.google.distancematrix.api;

import com.google.gson.GsonBuilder;
import com.lyft.cityguide.ApplicationConfiguration;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * GoogleDistanceMatrixAPIFactory
 * <p>
 */
@Module
public class GoogleDistanceMatrixAPIFactory {
    @Provides
    public IGoogleDistanceMatrixAPI provideJSONAPI(ApplicationConfiguration configuration) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.GOOGLE_DISTANCE_MATRIX_API_ENDPOINT)
            .setConverter(new GsonConverter(new GsonBuilder().create()))
            .build()
            .create(IGoogleDistanceMatrixAPI.class);
    }
}
