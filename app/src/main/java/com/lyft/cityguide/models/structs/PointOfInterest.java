package com.lyft.cityguide.models.structs;

import com.lyft.cityguide.models.beans.Distance;
import com.lyft.cityguide.models.beans.Place;

/**
 * @class PointOfInterest
 * @brief
 */
public class PointOfInterest {
    private Place    _place;
    private Distance _distance;

    public PointOfInterest(Place place, Distance distance) {
        _place = place;
        _distance = distance;
    }

    public Place getPlace() {
        return _place;
    }

    public Distance getDistance() {
        return _distance;
    }
}
