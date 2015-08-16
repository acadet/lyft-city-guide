package com.lyft.cityguide.models.bll;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

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
    private LocationManager _locationManager;
    private Location        _latestLocation;
    private String          _latestNextPageToken;

    PlaceBLL(Context context) {
        super(context);

        _locationManager = (LocationManager)
            getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private List<PointOfInterest> _toPOIS(List<Place> places) {
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

    @Override
    public void getBarsAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        Action0 fetchAction = () -> {
            connectAPI(
                (api) -> {
                    api.search(
                        latLngFromLocation(_latestLocation),
                        2000,
                        "bar",
                        getAPIKey(),
                        new BLLCallback<PlaceSearchResult>(failure) {
                            @Override
                            public void success(PlaceSearchResult placeSearchResult, Response response) {
                                List<PointOfInterest> outcome
                                    = _toPOIS(placeSearchResult.getResults());
                                _latestNextPageToken = placeSearchResult.getPageToken();

                                runOnMainThread(() -> success.run(outcome));
                            }
                        }
                    );
                },
                failure
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
                            failure.run(getContext().getString(R.string.error_no_network));
                        }
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    failure.run(getContext().getString(R.string.error_location_disabled));
                }
            },
            null
        );
    }

    @Override
    public void moreBarsAround(Action<List<PointOfInterest>> success, Action<String> failure) {
        if (_latestNextPageToken == null || _latestNextPageToken.isEmpty()) {
            runOnMainThread(() -> success.run(new ArrayList<>()));
            return;
        }

        connectAPI(
            (api) -> {
                api.more(
                    _latestNextPageToken,
                    getAPIKey(),
                    new BLLCallback<PlaceSearchResult>(failure) {
                        @Override
                        public void success(PlaceSearchResult placeSearchResult, Response response) {
                            List<PointOfInterest> outcome = _toPOIS(placeSearchResult.getResults());

                            _latestNextPageToken = placeSearchResult.getPageToken();
                            runOnMainThread(() -> success.run(outcome));
                        }
                    }
                );
            },
            failure
        );
    }

}
