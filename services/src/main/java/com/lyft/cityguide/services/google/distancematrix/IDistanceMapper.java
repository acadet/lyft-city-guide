package com.lyft.cityguide.services.google.distancematrix;

import com.lyft.cityguide.domain.Distance;

/**
 * IDistanceMapper
 * <p>
 */
interface IDistanceMapper {
    Distance map(Float source);
}
