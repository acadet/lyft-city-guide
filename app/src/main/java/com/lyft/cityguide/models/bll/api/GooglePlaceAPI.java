package com.lyft.cityguide.models.bll.api;

import com.lyft.cityguide.models.bll.structs.PlaceSearchResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @class GooglePlaceAPI
 * @brief https://developers.google.com/places/webservice/intro
 */
public interface GooglePlaceAPI {
    @GET("/nearbysearch/json")
    void search(
        @Query("location") String location,
        @Query("radius") float radius,
        @Query("types") String types,
        @Query("key") String apiKey,
        Callback<PlaceSearchResult> callback
    );

    @GET("/nearbysearch/json")
    void more(
        @Query("pagetoken") String pageToken,
        @Query("key") String apiKey,
        Callback<PlaceSearchResult> callback
    );
}
