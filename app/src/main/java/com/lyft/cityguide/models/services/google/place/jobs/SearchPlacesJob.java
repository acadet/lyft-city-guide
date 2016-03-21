package com.lyft.cityguide.models.services.google.place.jobs;

import android.location.Location;

import com.lyft.cityguide.SecretApplicationConfiguration;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.serializers.PointOfInterestBLLDTOSerializer;
import com.lyft.cityguide.models.services.RetrofitJob;
import com.lyft.cityguide.models.services.google.place.GooglePlaceErrors;
import com.lyft.cityguide.models.services.google.place.api.IGooglePlaceAPI;
import com.lyft.cityguide.models.services.google.place.dto.SearchOutcomeGooglePlaceDTO;

import java.util.List;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * SearchPlacesJob
 * <p>
 */
public class SearchPlacesJob extends RetrofitJob {
    private Observable<List<PointOfInterestBLLDTO>> searchObservable;

    private float    radiusInMeters;
    private String   type;
    private Location currentLocation;

    private Observable<List<PointOfInterestBLLDTO>> searchMoreObservable;

    private String nextPageToken;

    SearchPlacesJob(SecretApplicationConfiguration configuration, IGooglePlaceAPI api, PointOfInterestBLLDTOSerializer serializer) {
        searchObservable = Observable
            .create(new Observable.OnSubscribe<List<PointOfInterestBLLDTO>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterestBLLDTO>> subscriber) {
                    try {
                        SearchOutcomeGooglePlaceDTO outcome;

                        outcome = api.search(
                            currentLocation.getLatitude() + "," + currentLocation.getLongitude(),
                            radiusInMeters,
                            type,
                            configuration.GOOGLE_API_KEY
                        );

                        nextPageToken = outcome.getNextPageToken();

                        subscriber.onNext(serializer.fromPlaceGooglePlaceDTO(outcome.getPlaces()));
                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());

        searchMoreObservable = Observable
            .create(new Observable.OnSubscribe<List<PointOfInterestBLLDTO>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterestBLLDTO>> subscriber) {
                    if (nextPageToken == null) {
                        subscriber.onError(new GooglePlaceErrors.NoMoreResult());
                        return;
                    }

                    try {
                        SearchOutcomeGooglePlaceDTO outcome;

                        outcome = api.more(
                            configuration.GOOGLE_API_KEY,
                            nextPageToken
                        );

                        nextPageToken = outcome.getNextPageToken();

                        subscriber.onNext(serializer.fromPlaceGooglePlaceDTO(outcome.getPlaces()));
                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<List<PointOfInterestBLLDTO>> search(Location currentLocation, float radiusInMeters, String type) {
        this.currentLocation = currentLocation;
        this.radiusInMeters = radiusInMeters;
        this.type = type;

        return searchObservable;
    }

    public Observable<List<PointOfInterestBLLDTO>> more() {
        return searchMoreObservable;
    }
}
