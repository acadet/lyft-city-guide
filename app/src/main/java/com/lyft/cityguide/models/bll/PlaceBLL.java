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
import com.lyft.cityguide.models.beans.Distance;
import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.models.beans.RangeSetting;
import com.lyft.cityguide.models.bll.api.APIOutletFactory;
import com.lyft.cityguide.models.services.google.place.api.IGooglePlaceAPI;
import com.lyft.cityguide.models.bll.interfaces.IDistanceBLL;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;
import com.lyft.cityguide.models.bll.interfaces.ISettingsBLL;
import com.lyft.cityguide.models.bll.structs.PlaceSearchResult;
import com.lyft.cityguide.models.structs.PointOfInterest;
import com.lyft.cityguide.utils.DeviceHelper;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;

/**
 * @class PlaceBLL
 * @brief
 */
class PlaceBLL extends BaseBLL implements IPlaceBLL {
    private final static Object _asyncTaskLock = new Object();

    // Prevent app to fetch location anytime
    private final static int LOCATION_COOLDOWN_SEC = 20;

    private IDistanceBLL _distanceBLL;
    private ISettingsBLL _settingsBLL;

    private LocationManager _locationManager;
    private Location        _latestLocation;
    private DateTime        _latestLocationDate;
    private String          _latestNextPageToken;

    // Background tasks
    private AsyncTask _getBarsAroundTask;
    private AsyncTask _moreBarsAroundTask;

    private AsyncTask _getBistrosAroundTask;
    private AsyncTask _moreBistrosAroundTask;

    private AsyncTask _getCafesAroundTask;
    private AsyncTask _moreCafesAroundTask;

    PlaceBLL(Context context, IDistanceBLL distanceBLL, ISettingsBLL settingsBLL) {
        super(context);

        _distanceBLL = distanceBLL;
        _settingsBLL = settingsBLL;

        _locationManager = (LocationManager)
            getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Returns a list of POIs using places and distances
     *
     * @param places
     * @param distances
     * @return
     */
    private List<PointOfInterest> _toPOIs(List<Place> places, List<Distance> distances) {
        List<PointOfInterest> outcome = new ArrayList<>();
        int s2 = distances.size();

        for (int i = 0, s1 = places.size(); i < s1; i++) {
            Distance d;

            if (i < s2) {
                d = distances.get(i);
            } else { // No distance found, prevent index overflows
                d = new Distance();
                d.setDistance(0);
            }

            outcome.add(new PointOfInterest(places.get(i), d));
        }

        return outcome;
    }

    /**
     * Generic method for returning POIs
     *
     * @param success
     * @param failure
     * @param type             Type to fetch
     * @param taskPointer      Background task pointer
     * @param setTaskPointer   Action setting pointer
     * @param resetTaskPointer Action resetting pointer
     */
    private void _getPOIsAround(
        Action<List<PointOfInterest>> success, Action<String> failure,
        String type, final AsyncTask taskPointer, Action<AsyncTask> setTaskPointer,
        Action0 resetTaskPointer) {

        if (taskPointer != null) { // Abort similar task if any
            synchronized (_asyncTaskLock) {
                if (taskPointer != null) {
                    cancel(taskPointer);
                }
            }
        }

        setTaskPointer.run(
            runInBackground(
                () -> {
                    Action0 customWhenDone = () -> {
                        synchronized (_asyncTaskLock) {
                            whenDone(taskPointer);
                            resetTaskPointer.run();
                        }
                    };
                    Action<String> customFailure = (s) -> {
                        customWhenDone.run();
                        runOnMainThread(() -> failure.run(s));
                    };

                    // Action to trigger once the location has been engined
                    Action0 fetchAction = () -> {
                        connectAPI(
                            (api) -> {
                                int range;
                                RangeSetting setting = _settingsBLL.get();

                                switch (setting) {
                                    case ONE_MILE:
                                        range = 1;
                                        break;
                                    case TWO_MILE:
                                        range = 2;
                                        break;
                                    default:
                                        range = 5;
                                        break;
                                }

                                api.search(
                                    latLngFromLocation(_latestLocation),
                                    range * 1609,
                                    type,
                                    getAPIKey(),
                                    new BLLCallback<PlaceSearchResult>(customFailure) {
                                        @Override
                                        public void success(PlaceSearchResult placeSearchResult, Response response) {
                                            List<Place> places = placeSearchResult.getResults();

                                            _latestNextPageToken = placeSearchResult.getPageToken();

                                            if (places.size() > 0) {
                                                _distanceBLL.getDistances(
                                                    _latestLocation,
                                                    places,
                                                    (distances) -> {
                                                        List<PointOfInterest> outcome
                                                            = _toPOIs(places, distances);

                                                        customWhenDone.run();
                                                        runOnMainThread(() -> success.run(outcome));
                                                    },
                                                    customFailure
                                                );
                                            } else { // No matching place. Return an empty list
                                                List<PointOfInterest> outcome = new ArrayList<>();

                                                customWhenDone.run();
                                                runOnMainThread(() -> success.run(outcome));
                                            }
                                        }
                                    }
                                );
                            },
                            customFailure
                        );
                    };

                    _latestNextPageToken = null;

                    if (_latestLocationDate != null
                        && _latestLocationDate.plusSeconds(LOCATION_COOLDOWN_SEC).isAfterNow()) {
                        // Prevent too many location research
                        fetchAction.run();
                    } else {
                        if (!DeviceHelper.isNetworkAvailable(getContext())) {
                            // No connectivity
                            customFailure.run(getContext().getString(R.string.error_no_network));
                            return;
                        }

                        // This service requires the main thread to run
                        // Then, following actions are dispatched when
                        // the location has been engined
                        _locationManager.requestSingleUpdate(
                            LocationManager.NETWORK_PROVIDER,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    _latestLocation = location;
                                    _latestLocationDate = DateTime.now();

                                    customWhenDone.run();
                                    setTaskPointer.run(
                                        runInBackground(
                                            () -> {
                                                fetchAction.run();
                                            }
                                        )
                                    );
                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {
                                    if (status == LocationProvider.OUT_OF_SERVICE || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                                        if (_latestLocation != null) {
                                            // Use latest location if no connectivity
                                            customWhenDone.run();
                                            setTaskPointer.run(
                                                runInBackground(
                                                    () -> {
                                                        fetchAction.run();
                                                    }
                                                )
                                            );
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
                }
            )
        );
    }

    /**
     * Generic method for fetching more POIs around
     *
     * @param success
     * @param failure
     * @param taskPointer
     * @param setTaskPointer
     * @param resetTaskPointer
     */
    private void _morePOIsAround(
        Action<List<PointOfInterest>> success, Action<String> failure,
        final AsyncTask taskPointer, Action<AsyncTask> setTaskPointer,
        Action0 resetTaskPointer) {

        if (_latestNextPageToken == null || _latestNextPageToken.isEmpty()) {
            runOnMainThread(() -> success.run(new ArrayList<>()));
            return;
        }

        if (taskPointer != null) { // Prevent similar task
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
                                        List<Place> places = placeSearchResult.getResults();

                                        _latestNextPageToken = placeSearchResult.getPageToken();

                                        if (places.size() > 0) {
                                            _distanceBLL.getDistances(
                                                _latestLocation,
                                                places,
                                                (distances) -> {
                                                    List<PointOfInterest> outcome
                                                        = _toPOIs(places, distances);

                                                    synchronized (_asyncTaskLock) {
                                                        whenDone(taskPointer);
                                                        resetTaskPointer.run();
                                                    }
                                                    runOnMainThread(() -> success.run(outcome));
                                                },
                                                customFailure
                                            );
                                        } else {
                                            List<PointOfInterest> outcome = new ArrayList<>();

                                            synchronized (_asyncTaskLock) {
                                                whenDone(taskPointer);
                                                resetTaskPointer.run();
                                            }
                                            runOnMainThread(() -> success.run(outcome));
                                        }
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

    void connectAPI(Action<IGooglePlaceAPI> success, Action<String> failure) {
        APIOutletFactory
            .googlePlace(getContext())
            .connect(
                (api) -> success.run(api),
                () -> failure.run(getContext().getString(R.string.error_no_network))
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

    @Override
    public void cancelAllTasks() {
        super.cancelAllTasks();
        // As this class uses the distanceBLL, abort its pending operations too
        _distanceBLL.cancelAllTasks();
    }
}
