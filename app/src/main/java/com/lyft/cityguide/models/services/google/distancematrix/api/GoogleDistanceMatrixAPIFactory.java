package com.lyft.cityguide.models.services.google.distancematrix.api;

import com.google.gson.GsonBuilder;
import com.lyft.cityguide.ApplicationConfiguration;
import com.lyft.cityguide.models.services.google.distancematrix.dto.DistanceGoogleDistanceMatrixDTO;

import javax.inject.Singleton;

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
    public IGoogleDistanceMatrixAPI provideJSONAPI(ApplicationConfiguration configuration, DistanceGoogleDistanceMatrixDTOSerializer serializer) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.GOOGLE_DISTANCE_MATRIX_API_ENDPOINT)
            .setConverter(
                new GsonConverter(
                    new GsonBuilder()
                        .registerTypeAdapter(DistanceGoogleDistanceMatrixDTO.class, serializer)
                        .create()
                )
            )
            .build()
            .create(IGoogleDistanceMatrixAPI.class);
    }

    @Provides
    @Singleton
    public DistanceGoogleDistanceMatrixDTOSerializer provideSerializer() {
        return new DistanceGoogleDistanceMatrixDTOSerializer();
    }
}
