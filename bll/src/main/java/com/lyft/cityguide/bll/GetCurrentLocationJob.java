package com.lyft.cityguide.bll;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * GetCurrentLocationJob
 * <p>
 */
class GetCurrentLocationJob {
    private final Configuration   configuration;
    private final LocationManager locationManager;

    private DateTime latestLocationDate;
    private Location latestLocation;

    GetCurrentLocationJob(Configuration configuration, LocationManager locationManager) {
        this.configuration = configuration;
        this.locationManager = locationManager;
    }

    Observable<Location> create() {
        return Observable
            .create(new Observable.OnSubscribe<Location>() {
                @Override
                public void call(Subscriber<? super Location> subscriber) {
                    if (latestLocationDate != null
                        && latestLocationDate.plusSeconds(configuration.LOCATION_FETCHING_COOLDOWN_IN_SECONDS).isAfterNow()) {
                        subscriber.onNext(latestLocation);
                        subscriber.onCompleted();
                        return;
                    }

                    // This service requires the main thread to run
                    try {
                        locationManager.requestSingleUpdate(
                            LocationManager.NETWORK_PROVIDER,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    // No lock needed for running on a single thread
                                    latestLocation = location;
                                    latestLocationDate = DateTime.now();

                                    subscriber.onNext(latestLocation);
                                    subscriber.onCompleted();
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                    if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                                        if (latestLocation != null) { // Use latest location if no connectivity
                                            subscriber.onNext(latestLocation);
                                            subscriber.onCompleted();
                                            return;
                                        }
                                        subscriber.onError(new BLLErrors.NoConnection());
                                    }
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                    // Nothing to do
                                }

                                @Override
                                public void onProviderDisabled(String provider) {
                                    subscriber.onError(new BLLErrors.DisabledLocation());
                                }
                            },
                            Looper.getMainLooper()
                        );
                    } catch (SecurityException e) {
                        subscriber.onError(new BLLErrors.DisabledLocation());
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }
}
