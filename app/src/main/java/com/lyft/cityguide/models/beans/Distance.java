package com.lyft.cityguide.models.beans;

/**
 * @class Distance
 * @brief
 */
public class Distance {
    private float _distance; // stored in meters

    public float getDistance() {
        return _distance;
    }

    public void setDistance(float value) {
        _distance = value;
    }

    public float toMiles() {
        return _distance / 1609;
    }
}
