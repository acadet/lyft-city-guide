package com.lyft.cityguide.models.bll.serializers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * BLLSerializerFactory
 * <p>
 */
@Module
public class BLLSerializerFactory {
    @Provides
    @Singleton
    public PointOfInterestBLLDTOSerializer providePointOfInterestBLLDTOSerializer() {
        return new PointOfInterestBLLDTOSerializer();
    }
}
