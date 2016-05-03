package com.lyft.cityguide.services.google.distancematrix;

import android.content.Context;

import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * GoogleDistanceMatrixServiceFactory
 * <p>
 */
@Module
public class GoogleDistanceMatrixServiceFactory {

    @Provides
    @Singleton
    Configuration provideConfiguration(Context context) {
        return new Configuration(context);
    }

    @Provides
    @Singleton
    IDistanceMapper provideDistanceMapper() {
        return new DistanceMapper();
    }

    @Provides
    IGoogleDistanceMatrixAPI provideJSONAPI(Configuration configuration, DistanceDTOSerializer serializer) {
        return new RestAdapter.Builder()
            .setEndpoint(configuration.API_ENDPOINT)
            .setConverter(
                new GsonConverter(
                    new GsonBuilder()
                        .registerTypeAdapter(DistanceDTO.class, serializer)
                        .create()
                )
            )
            .build()
            .create(IGoogleDistanceMatrixAPI.class);
    }

    @Provides
    @Singleton
    DistanceDTOSerializer provideDistanceDTOSerializer() {
        return new DistanceDTOSerializer();
    }

    @Provides
    @Singleton
    FetchDistancesJob provideFetchDistancesJob(Configuration configuration, IGoogleDistanceMatrixAPI api, IDistanceMapper distanceMapper) {
        return new FetchDistancesJob(configuration, api, distanceMapper);
    }

    @Provides
    @Singleton
    public IGoogleDistanceMatrixService provideService(FetchDistancesJob fetchDistancesJob) {
        return new GoogleDistanceMatrixService(fetchDistancesJob);
    }
}
