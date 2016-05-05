package com.lyft.cityguide.domain;

/**
 * Distance
 */
public class Distance {
    private float valueInMeters;

    private Distance(float valueInMeters) {
        this.valueInMeters = valueInMeters;
    }

    public float toMeters() {
        return valueInMeters;
    }

    public float toMiles() {
        return valueInMeters / 1609;
    }

    public static Distance fromMeters(float value) {
        return new Distance(value);
    }
}
