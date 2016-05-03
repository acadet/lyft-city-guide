package com.lyft.cityguide.services.google.distancematrix;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * IGoogleDistanceMatrixAPI
 * <p>
 * https://developers.google.com/maps/documentation/distancematrix/intro
 */
interface IGoogleDistanceMatrixAPI {
    @GET("/json")
    DistanceDTO getDistances(
        @Query("origins") String origins,
        @Query("destinations") String destinations,
        @Query("mode") String mode,
        @Query("language") String language,
        @Query("key") String apiKey
    );
}
