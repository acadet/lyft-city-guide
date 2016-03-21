package com.lyft.cityguide.models.bll.jobs;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;

import com.lyft.cityguide.ApplicationConfiguration;
import com.lyft.cityguide.models.bll.BLLErrors;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * GetCurrentLocationJob
 * <p>
 */
public class GetCurrentLocationJob {
    private Observable<Location> observable;

    private DateTime latestLocationDate;
    private Location latestLocation;

    GetCurrentLocationJob(ApplicationConfiguration configuration, Context context) {
        observable = Observable
            .create(new Observable.OnSubscribe<Location>() {
                @Override
                public void call(Subscriber<? super Location> subscriber) {
                    LocationManager locationManager;

                    if (latestLocationDate != null && latestLocationDate.plusSeconds(configuration.LOCATION_FETCHING_COOLDOWN_IN_SECONDS)
                                                                        .isAfterNow()) {
                        subscriber.onNext(latestLocation);
                        subscriber.onCompleted();
                        return;
                    }

                    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                    // This service requires the main thread to run
                    locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
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
                                    }
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
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<Location> get() {
        return observable;
    }
}
