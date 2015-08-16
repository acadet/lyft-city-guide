package com.lyft.cityguide.models.bll;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;
import com.lyft.cityguide.models.bll.utils.PlaceSearchResult;
import com.lyft.cityguide.models.structs.PointOfInterest;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

/**
 * @class PlaceBLL
 * @brief
 */
class PlaceBLL extends BaseBLL implements IPlaceBLL {
    private final static Object _asyncTaskLock = new Object();

    private LocationManager _locationManager;
    private Location        _latestLocation;
    private String          _latestNextPageToken;

    private AsyncTask _getBarsAroundTask;
    private AsyncTask _moreBarsAroundTask;

    private AsyncTask _getBistrosAroundTask;
    private AsyncTask _moreBistrosAroundTask;

    private AsyncTask _getCafesAroundTask;
    private AsyncTask _moreCafesAroundTask;

    PlaceBLL(Context context) {
        super(context);

        _locationManager = (LocationManager)
            getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private List<PointOfInterest> _toPOIs(List<Place> places) {
        List<PointOfInterest> outcome = new ArrayList<>();

        for (Place p : places) {
            outcome.add(new PointOfInterest(p, _getDistance(p)));
        }

        return outcome;
    }

    private double _getDistance(Place place) {
        double latA, latB, longA, longB;

        latA = _latestLocation.getLatitude();
        longA = _latestLocation.getLongitude();
        latB = place.getLatitude();
        longB = place.getLongitude();

        return Math.round(
            Math.acos(
                Math.sin(latA) * Math.sin(latB)
                + Math.cos(latA) * Math.cos(latB) * Math.cos(longB - longA)
            ) * 100d
        ) / 100d;
    }

    private void _getPOIsAround(
        Action<List<PointOfInterest>> success, Action<String> failure,
        String type, final AsyncTask taskPointer, Action<AsyncTask> setTaskPointer,
        Action0 resetTaskPointer) {

        if (taskPointer != null) {
            synchronized (_asyncTaskLock) {
                if (taskPointer != null) {
                    cancel(taskPointer);
                }
            }
        }

        setTaskPointer.run(
            runInBackground(
                () -> {
                    Action<String> customFailure = (s) -> {
                        synchronized (_asyncTaskLock) {
                            whenDone(taskPointer);
                            resetTaskPointer.run();
                        }
                        runOnMainThread(() -> failure.run(s));
                    };

                    Action0 fetchAction = () -> {
                        connectAPI(
                            (api) -> {
                                api.search(
                                    latLngFromLocation(_latestLocation),
                                    2000,
                                    type,
                                    getAPIKey(),
                                    new BLLCallback<PlaceSearchResult>(customFailure) {
                                        @Override
                                        public void success(PlaceSearchResult placeSearchResult, Response response) {
                                            List<PointOfInterest> outcome
                                                = _toPOIs(placeSearchResult.getResults());
                                            _latestNextPageToken = placeSearchResult.getPageToken();

                                            synchronized (_asyncTaskLock) {
                                                whenDone(taskPointer);
                                                resetTaskPointer.run();
                                            }
                                            runOnMainThread(() -> success.run(outcome));
                                        }
                                    }
                                );
                            },
                            customFailure
                        );
                    };

                    _latestNextPageToken = null;

                    _locationManager.requestSingleUpdate(
                        LocationManager.NETWORK_PROVIDER,
                        new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                _latestLocation = location;

                                fetchAction.run();
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                                    if (_latestLocation != null) {
                                        fetchAction.run();
                                    } else {
                                        customFailure.run(getContext().getString(R.string.error_no_network));
                                    }
                                }
                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                customFailure.run(getContext().getString(R.string.error_location_disabled));
                            }
                        },
                        Looper.getMainLooper()
                    );
                }
            )
        );
    }

    private void _morePOIsAround(
        Action<List<PointOfInterest>> success, Action<String> failure,
        final AsyncTask taskPointer, Action<AsyncTask> setTaskPointer,
        Action0 resetTaskPointer) {

        if (_latestNextPageToken == null || _latestNextPageToken.isEmpty()) {
            runOnMainThread(() -> success.run(new ArrayList<>()));
            return;
        }

        if (taskPointer != null) {
            synchronized (_asyncTaskLock) {
                if (taskPointer != null) {
                    cancel(taskPointer);
                }
            }
        }

        setTaskPointer.run(
            runInBackground(
                () -> {
                    Action<String> customFailure = (s) -> {
                        synchronized (_asyncTaskLock) {
                            whenDone(taskPointer);
                            resetTaskPointer.run();
                        }
                        runOnMainThread(() -> failure.run(s));
                    };

                    connectAPI(
                        (api) -> {
                            api.more(
                                _latestNextPageToken,
                                getAPIKey(),
                                new BLLCallback<PlaceSearchResult>(customFailure) {
                                    @Override
                                    public void success(PlaceSearchResult placeSearchResult, Response response) {
                                        List<PointOfInterest> outcome = _toPOIs(
                                            placeSearchResult.getResults()
                                        );

                                        _latestNextPageToken = placeSearchResult.getPageToken();
                                        synchronized (_asyncTaskLock) {
                                            whenDone(taskPointer);
                                            resetTaskPointer.run();
                                        }
                                        runOnMainThread(() -> success.run(outcome));
                                    }
                                }
                            );
                        },
                        customFailure
                    );
                }
            )
        );
    }

    @Override
    public void getBarsAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _getPOIsAround(
            success,
            failure,
            "bar",
            _getBarsAroundTask,
            (t) -> _getBarsAroundTask = t,
            () -> _getBarsAroundTask = null
        );
    }

    @Override
    public void moreBarsAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _morePOIsAround(
            success,
            failure,
            _moreBarsAroundTask,
            (t) -> _moreBarsAroundTask = t,
            () -> _moreBarsAroundTask = null
        );
    }

    @Override
    public void getBistrosAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _getPOIsAround(
            success,
            failure,
            "restaurant",
            _getBistrosAroundTask,
            (t) -> _getBistrosAroundTask = t,
            () -> _getBistrosAroundTask = null
        );
    }

    @Override
    public void moreBistrosAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _morePOIsAround(
            success,
            failure,
            _moreBistrosAroundTask,
            (t) -> _moreBistrosAroundTask = t,
            () -> _moreBistrosAroundTask = null
        );
    }

    @Override
    public void getCafesAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _getPOIsAround(
            success,
            failure,
            "cafe",
            _getCafesAroundTask,
            (t) -> _getCafesAroundTask = t,
            () -> _getCafesAroundTask = null
        );
    }

    @Override
    public void moreCafesAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        _morePOIsAround(
            success,
            failure,
            _moreCafesAroundTask,
            (t) -> _moreCafesAroundTask = t,
            () -> _moreCafesAroundTask = null
        );
    }
}
