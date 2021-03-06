package com.lyft.cityguide.services.google.place;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;
import com.lyft.cityguide.services.RetrofitJob;

import java.util.List;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * SearchPlacesJob
 * <p>
 */
class SearchPlacesJob extends RetrofitJob {
    private final Configuration          configuration;
    private final IGooglePlaceAPI        api;
    private final IPointOfInterestMapper mapper;

    private String               nextPageToken;
    private PointOfInterest.Kind currentKind;

    SearchPlacesJob(Configuration configuration, IGooglePlaceAPI api, IPointOfInterestMapper mapper) {
        this.configuration = configuration;
        this.api = api;
        this.mapper = mapper;
    }

    Observable<List<PointOfInterest>> search(Location currentLocation, SearchRangeSetting searchRangeSetting, PointOfInterest.Kind kind) {
        return Observable
            .create(new Observable.OnSubscribe<List<PointOfInterest>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterest>> subscriber) {
                    try {
                        float radiusInMeters;
                        SearchOutcomeDTO outcome;

                        switch (searchRangeSetting) {
                            case ONE_MILE:
                                radiusInMeters = 1;
                                break;
                            case TWO_MILE:
                                radiusInMeters = 2;
                                break;
                            case FIVE_MILE:
                                radiusInMeters = 5;
                                break;
                            default:
                                radiusInMeters = 1;
                                Timber.e("Unexpected search range setting");
                                break;
                        }

                        radiusInMeters *= 1609;

                        currentKind = kind;

                        outcome = api.search(
                            currentLocation.getLatitude() + "," + currentLocation.getLongitude(),
                            radiusInMeters,
                            mapper.map(currentKind),
                            configuration.API_KEY
                        );

                        nextPageToken = outcome.getNextPageToken();

                        subscriber.onNext(mapper.map(outcome.getPlaces(), currentKind));
                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    Observable<List<PointOfInterest>> more() {
        return Observable
            .create(new Observable.OnSubscribe<List<PointOfInterest>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterest>> subscriber) {
                    if (nextPageToken == null) {
                        subscriber.onError(new GooglePlaceErrors.NoMoreResult());
                        return;
                    }

                    try {
                        SearchOutcomeDTO outcome;

                        outcome = api.more(
                            nextPageToken,
                            configuration.API_KEY
                        );

                        if (!subscriber.isUnsubscribed()) {
                            // If request is cancelled or restarted, do not save new token
                            nextPageToken = outcome.getNextPageToken();
                        }

                        subscriber.onNext(mapper.map(outcome.getPlaces(), currentKind));
                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }
}
