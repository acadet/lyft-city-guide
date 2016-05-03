package com.lyft.cityguide.domain;

/**
 * PointOfInterest
 * <p>
 * Wraps place + distance for the UI layer
 */
public class PointOfInterest {
    public enum Kind {
        BAR, BISTRO, CAFE
    }

    private String id;
    private float  latitude;
    private float  longitude;
    private String name;
    private float  rating;
    private Distance distance;
    private Kind kind;

    public String getId() {
        return id;
    }

    public PointOfInterest setId(String id) {
        this.id = id;
        return this;
    }

    public float getLatitude() {
        return latitude;
    }

    public PointOfInterest setLatitude(float latitude) {
        this.latitude = latitude;
        return this;
    }

    public float getLongitude() {
        return longitude;
    }

    public PointOfInterest setLongitude(float longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getName() {
        return name;
    }

    public PointOfInterest setName(String name) {
        this.name = name;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public PointOfInterest setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public Distance getDistance() {
        return distance;
    }

    public PointOfInterest setDistance(Distance distance) {
        this.distance = distance;
        return this;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }
}
