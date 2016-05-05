package com.lyft.cityguide.services.google.distancematrix;

import com.lyft.cityguide.domain.Distance;

/**
 * DistanceMapper
 * <p>
 */
class DistanceMapper implements IDistanceMapper {
    @Override
    public Distance map(Float source) {
        return Distance.fromMeters(source);
    }
}
