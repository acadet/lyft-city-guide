package com.lyft.cityguide.models.bll.dto;

/**
 * PointOfInterestBLLDTO
 * <p>
 * Wraps place + distance for the UI layer
 */
public class PointOfInterestBLLDTO {
    private String id;
    private float  latitude;
    private float  longitude;
    private String name;
    private float  rating;

    private DistanceBLLDTO distance;

    public String getId() {
        return id;
    }

    public PointOfInterestBLLDTO setId(String id) {
        this.id = id;
        return this;
    }

    public float getLatitude() {
        return latitude;
    }

    public PointOfInterestBLLDTO setLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public float getLongitude() {
        return longitude;
    }

    public PointOfInterestBLLDTO setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getName() {
        return name;
    }

    public PointOfInterestBLLDTO setName(String name) {
        this.name = name;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public PointOfInterestBLLDTO setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public DistanceBLLDTO getDistance() {
        return distance;
    }

    public PointOfInterestBLLDTO setDistance(DistanceBLLDTO distance) {
        this.distance = distance;
        return this;
    }
}
