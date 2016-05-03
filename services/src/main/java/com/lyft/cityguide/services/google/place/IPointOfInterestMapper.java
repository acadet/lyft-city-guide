package com.lyft.cityguide.services.google.place;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

/**
 * IPointOfInterestMapper
 * <p>
 */
interface IPointOfInterestMapper {
    PointOfInterest map(PlaceDTO source);

    List<PointOfInterest> map(List<PlaceDTO> source);
}
