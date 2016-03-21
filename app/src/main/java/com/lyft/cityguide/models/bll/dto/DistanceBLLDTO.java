package com.lyft.cityguide.models.bll.dto;

/**
 * DistanceBLLDTO
 */
public class DistanceBLLDTO {
    private float valueInMeters;

    private DistanceBLLDTO(float valueInMeters) {
        this.valueInMeters = valueInMeters;
    }

    public float toMeters() {
        return valueInMeters;
    }

    public float toMiles() {
        return valueInMeters / 1609;
    }

    public static DistanceBLLDTO fromMeters(float value) {
        return new DistanceBLLDTO(value);
    }
}
