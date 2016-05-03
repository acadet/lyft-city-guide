package com.lyft.cityguide.services.google.distancematrix;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.services.RetrofitJob;

import java.util.List;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * FetchDistancesJob
 * <p>
 */
class FetchDistancesJob extends RetrofitJob {
    private final Configuration            configuration;
    private final IGoogleDistanceMatrixAPI api;
    private final IDistanceMapper          distanceMapper;

    FetchDistancesJob(Configuration configuration, IGoogleDistanceMatrixAPI api, IDistanceMapper distanceMapper) {
        this.configuration = configuration;
        this.api = api;
        this.distanceMapper = distanceMapper;
    }

    public Observable<Void> create(Location currentLocation, List<PointOfInterest> pointOfInterests) {
        return Observable
            .create(new Observable.OnSubscribe<Void>() {
                @Override
                public void call(Subscriber<? super Void> subscriber) {
                    try {
                        StringBuffer destinations = new StringBuffer();
                        int i = 0, size = pointOfInterests.size();
                        DistanceDTO outcome;

                        // Format data for the API
                        for (PointOfInterest p : pointOfInterests) {
                            destinations
                                .append(p.getLatitude())
                                .append(",")
                                .append(p.getLongitude());

                            if (i < size - 1) {
                                destinations.append("|");
                            }
                            i++;
                        }

                        outcome = api.getDistances(
                            currentLocation.getLatitude() + "," + currentLocation.getLongitude(),
                            destinations.toString(),
                            "walking",
                            "en",
                            configuration.API_KEY
                        );

                        size = Math.min(size, outcome.getDistances().size());
                        for (i = 0; i < size; i++) {
                            pointOfInterests.get(i).setDistance(distanceMapper.map(outcome.getDistances().get(i)));
                        }

                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }
}
