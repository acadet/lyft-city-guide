package com.lyft.cityguide.services.google.place;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

import timber.log.Timber;

/**
 * PointOfInterestMapper
 * <p>
 */
class PointOfInterestMapper implements IPointOfInterestMapper {
    @Override
    public PointOfInterest.Kind map(String source) {
        PointOfInterest.Kind outcome;

        if (source.equals("bar")) {
            outcome = PointOfInterest.Kind.BAR;
        } else if (source.equals("restaurant")) {
            outcome = PointOfInterest.Kind.BISTRO;
        } else if (source.equals("cafe")) {
            outcome = PointOfInterest.Kind.CAFE;
        } else {
            outcome = PointOfInterest.Kind.BAR;
            Timber.e("Unexpected string for kind");
        }

        return outcome;
    }

    @Override
    public String map(PointOfInterest.Kind source) {
        String outcome;

        switch (source) {
            case BAR:
                outcome = "bar";
                break;
            case BISTRO:
                outcome = "restaurant";
                break;
            case CAFE:
                outcome = "cafe";
                break;
            default:
                outcome = "bar";
                Timber.e("Unexpected value for kind");
                break;
        }

        return outcome;
    }

    @Override
    public PointOfInterest map(PlaceDTO source, PointOfInterest.Kind kind) {
        return new PointOfInterest()
            .setId(source.getId())
            .setLatitude(source.getLatitude())
            .setLongitude(source.getLongitude())
            .setName(source.getName())
            .setRating(source.getRating())
            .setKind(kind);
    }

    @Override
    public List<PointOfInterest> map(List<PlaceDTO> source, PointOfInterest.Kind kind) {
        return Stream.of(source).map((e) -> map(e, kind)).collect(Collectors.toList());
    }
}
