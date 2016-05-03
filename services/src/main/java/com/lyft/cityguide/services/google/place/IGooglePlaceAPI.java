package com.lyft.cityguide.services.google.place;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * IGooglePlaceAPI
 * <p>
 * https://developers.google.com/places/webservice/intro
 */
interface IGooglePlaceAPI {
    @GET("/nearbysearch/json")
    SearchOutcomeGooglePlaceDTO search(
        @Query("location") String location,
        @Query("radius") float radius,
        @Query("types") String types,
        @Query("key") String apiKey
    );

    @GET("/nearbysearch/json")
    SearchOutcomeGooglePlaceDTO more(
        @Query("pagetoken") String pageToken,
        @Query("key") String apiKey
    );
}
