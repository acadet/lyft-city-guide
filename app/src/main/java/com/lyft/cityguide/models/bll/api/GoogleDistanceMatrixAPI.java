package com.lyft.cityguide.models.bll.api;

import com.lyft.cityguide.models.bll.structs.DistanceResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @class GoogleDistanceMatrixAPI
 * @brief https://developers.google.com/maps/documentation/distancematrix/intro
 */
public interface GoogleDistanceMatrixAPI {
    @GET("/json")
    void getDistances(
        @Query("origins") String origins,
        @Query("destinations") String destinations,
        @Query("mode") String mode,
        @Query("language") String language,
        @Query("units") String units,
        @Query("key") String apiKey,
        Callback<DistanceResult> callback
    );
}
