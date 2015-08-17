package com.lyft.cityguide.models.bll;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.beans.Distance;
import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.models.bll.api.APIOutletFactory;
import com.lyft.cityguide.models.bll.api.GoogleDistanceMatrixAPI;
import com.lyft.cityguide.models.bll.interfaces.IDistanceBLL;
import com.lyft.cityguide.models.bll.structs.DistanceResult;
import com.lyft.cityguide.utils.actions.Action;

import java.util.List;

import retrofit.client.Response;

/**
 * @class DistanceBLL
 * @brief
 */
class DistanceBLL extends BaseBLL implements IDistanceBLL {
    private final static Object _asyncTaskLock = new Object();

    private AsyncTask _getDistancesTask;

    DistanceBLL(Context context) {
        super(context);
    }

    void connectAPI(Action<GoogleDistanceMatrixAPI> success, Action<String> failure) {
        APIOutletFactory
            .googleDistanceMatrix(getContext())
            .connect(
                (api) -> success.run(api),
                () -> failure.run(getContext().getString(R.string.error_no_network))
            );
    }

    @Override
    public void getDistances(Location currentLocation, List<Place> places, Action<List<Distance>> success, Action<String> failure) {
        if (_getDistancesTask != null) { // Cancel same task if any
            synchronized (_asyncTaskLock) {
                if (_getDistancesTask != null) {
                    cancel(_getDistancesTask);
                }
            }
        }

        _getDistancesTask = runInBackground(
            () -> {
                Action<String> customFailure = (error) -> {
                    synchronized (_asyncTaskLock) {
                        whenDone(_getDistancesTask);
                        _getDistancesTask = null;
                    }
                    runOnMainThread(() -> failure.run(error));
                };
                StringBuffer destinations = new StringBuffer();

                // Format data for the API
                for (int i = 0, s = places.size(); i < s; i++) {
                    Place p = places.get(i);

                    destinations
                        .append(p.getLatitude())
                        .append(",")
                        .append(p.getLongitude());

                    if (i < s - 1) {
                        destinations.append("|");
                    }
                }

                connectAPI(
                    (api) -> {
                        api.getDistances(
                            latLngFromLocation(currentLocation),
                            destinations.toString(),
                            "walking",
                            "en",
                            "imperial",
                            getContext().getString(R.string.google_service_api_key),
                            new BLLCallback<DistanceResult>(customFailure) {
                                @Override
                                public void success(DistanceResult distanceResult, Response response) {
                                    synchronized (_asyncTaskLock) {
                                        whenDone(_getDistancesTask);
                                        _getDistancesTask = null;
                                    }
                                    runOnMainThread(() -> success.run(distanceResult.getDistances()));
                                }
                            }
                        );
                    },
                    customFailure
                );
            }
        );
    }
}
