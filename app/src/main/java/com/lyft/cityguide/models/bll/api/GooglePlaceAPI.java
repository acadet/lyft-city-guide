package com.lyft.cityguide.models.bll.api;

import com.lyft.cityguide.models.bll.utils.PlaceSearchResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @class GooglePlaceAPI
 * @brief
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
}
