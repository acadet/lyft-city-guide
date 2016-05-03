package com.lyft.cityguide.services.google.place.dto;

/**
 * PlaceGooglePlaceDTO
 * <p>
 */
public class PlaceGooglePlaceDTO {
    private String id;
    private float  latitude;
    private float  longitude;
    private String name;
    private float  rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
