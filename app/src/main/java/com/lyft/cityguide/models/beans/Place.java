package com.lyft.cityguide.models.beans;

/**
 * @class Place
 * @brief
 */
public class Place {
    private String id;

    private float  latitude;
    private float  longitude;
    private String name;
    private float  rating;

    public float getLatitude() {
        return latitude;
    }

    public String getId() {
        return id;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
