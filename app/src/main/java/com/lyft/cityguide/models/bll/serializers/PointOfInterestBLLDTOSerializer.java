package com.lyft.cityguide.models.bll.serializers;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.services.google.place.dto.PlaceGooglePlaceDTO;

import java.util.List;

/**
 * PointOfInterestBLLDTOSerializer
 * <p>
 */
public class PointOfInterestBLLDTOSerializer {
    public PointOfInterestBLLDTO fromPlaceGooglePlaceDTO(PlaceGooglePlaceDTO dto) {
        return new PointOfInterestBLLDTO()
            .setId(dto.getId())
            .setLatitude(dto.getLatitude())
            .setLongitude(dto.getLongitude())
            .setName(dto.getName())
            .setRating(dto.getRating());
    }

    public List<PointOfInterestBLLDTO> fromPlaceGooglePlaceDTO(List<PlaceGooglePlaceDTO> list) {
        return Stream
            .of(list)
            .map(this::fromPlaceGooglePlaceDTO)
            .collect(Collectors.toList());
    }
}
