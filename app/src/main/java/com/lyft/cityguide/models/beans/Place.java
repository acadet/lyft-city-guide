package com.lyft.cityguide.models.beans;

/**
 * @class Place
 * @brief
 */
public class Place {
    private String _id;

    private float  _latitude;
    private float  _longitude;
    private String _name;
    private float  _rating;

    public float getLatitude() {
        return _latitude;
    }

    public String getId() {
        return _id;
    }

    public float getLongitude() {
        return _longitude;
    }

    public String getName() {
        return _name;
    }

    public float getRating() {
        return _rating;
    }

    public void setId(String id) {
        this._id = id;
    }

    public void setLatitude(float latitude) {
        this._latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this._longitude = longitude;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setRating(float rating) {
        this._rating = rating;
    }
}
