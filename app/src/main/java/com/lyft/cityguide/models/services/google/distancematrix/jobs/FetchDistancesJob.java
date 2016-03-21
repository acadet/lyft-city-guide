package com.lyft.cityguide.models.services.google.distancematrix.jobs;

import android.location.Location;

import com.lyft.cityguide.SecretApplicationConfiguration;
import com.lyft.cityguide.models.bll.dto.DistanceBLLDTO;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.services.RetrofitJob;
import com.lyft.cityguide.models.services.google.distancematrix.api.IGoogleDistanceMatrixAPI;
import com.lyft.cityguide.models.services.google.distancematrix.dto.DistanceGoogleDistanceMatrixDTO;

import java.util.List;

import retrofit.RetrofitError;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * FetchDistancesJob
 * <p>
 */
public class FetchDistancesJob extends RetrofitJob {
    private Observable<Void>            observable;
    private Location                    currentLocation;
    private List<PointOfInterestBLLDTO> pointOfInterests;

    FetchDistancesJob(SecretApplicationConfiguration configuration, IGoogleDistanceMatrixAPI api) {
        observable = Observable
            .create(new Observable.OnSubscribe<Void>() {
                @Override
                public void call(Subscriber<? super Void> subscriber) {
                    try {
                        StringBuffer destinations = new StringBuffer();
                        int i = 0, size = pointOfInterests.size();
                        DistanceGoogleDistanceMatrixDTO outcome;

                        // Format data for the API
                        for (PointOfInterestBLLDTO p : pointOfInterests) {
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
                            configuration.GOOGLE_API_KEY
                        );

                        size = Math.min(size, outcome.getDistances().size());
                        for (i = 0; i < size; i++) {
                            pointOfInterests.get(i).setDistance(DistanceBLLDTO.fromMeters(outcome.getDistances().get(i)));
                        }

                        subscriber.onCompleted();
                    } catch (RetrofitError e) {
                        handleError(e, subscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<Void> get(Location currentLocation, List<PointOfInterestBLLDTO> pointOfInterests) {
        this.currentLocation = currentLocation;
        this.pointOfInterests = pointOfInterests;
        return observable;
    }
}
