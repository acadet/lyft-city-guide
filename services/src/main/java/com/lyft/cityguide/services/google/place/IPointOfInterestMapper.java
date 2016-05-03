package com.lyft.cityguide.services.google.place;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

/**
 * IPointOfInterestMapper
 * <p>
 */
interface IPointOfInterestMapper {
    PointOfInterest.Kind map(String source);

    String map(PointOfInterest.Kind source);

    PointOfInterest map(PlaceDTO source, PointOfInterest.Kind kind);

    List<PointOfInterest> map(List<PlaceDTO> source, PointOfInterest.Kind kind);
}
