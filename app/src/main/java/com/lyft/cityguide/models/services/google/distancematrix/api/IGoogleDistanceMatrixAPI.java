package com.lyft.cityguide.models.services.google.distancematrix.api;

import com.lyft.cityguide.models.services.google.distancematrix.dto.DistanceGoogleDistanceMatrixDTO;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * IGoogleDistanceMatrixAPI
 * <p>
 * https://developers.google.com/maps/documentation/distancematrix/intro
 */
public interface IGoogleDistanceMatrixAPI {
    @GET("/json")
    DistanceGoogleDistanceMatrixDTO getDistances(
        @Query("origins") String origins,
        @Query("destinations") String destinations,
        @Query("mode") String mode,
        @Query("language") String language,
        @Query("key") String apiKey
    );
}
