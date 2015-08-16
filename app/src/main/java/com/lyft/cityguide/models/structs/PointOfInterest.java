package com.lyft.cityguide.models.structs;

import com.lyft.cityguide.models.beans.Place;

/**
 * @class PointOfInterest
 * @brief
 */
public class PointOfInterest {
    private Place  _place;
    private double _distance;

    public PointOfInterest(Place place, double distance) {
        _place = place;
        _distance = distance;
    }

    public Place getPlace() {
        return _place;
    }

    public double getDistance() {
        return _distance;
    }
}
