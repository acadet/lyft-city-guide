package com.lyft.cityguide.models.bll;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.api.GooglePlaceAPI;
import com.lyft.cityguide.models.bll.api.GooglePlaceAPIOutletFactory;
import com.lyft.cityguide.models.bll.interfaces.IBLL;
import com.lyft.cityguide.models.bll.utils.BackgroundTask;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * @class BaseBLL
 * @brief
 */
class BaseBLL implements IBLL {
    private Context                     _context;
    private Map<String, BackgroundTask> _backgroundTasks;

    abstract class BLLCallback<T> implements Callback<T> {
        private Action<String> _failure;

        public BLLCallback(Action<String> failure) {
            this._failure = failure;
        }

        @Override
        public void failure(RetrofitError error) {
            processError(error, _failure);
        }
    }

    BaseBLL(Context context) {
        _context = context;
        _backgroundTasks = new HashMap<>();
    }

    Context getContext() {
        return _context;
    }

    String getAPIKey() {
        return getContext().getString(R.string.api_key);
    }

    String latLngFromLocation(Location l) {
        return new Double(l.getLatitude()).toString() + ',' + new Double(l.getLongitude()).toString();
    }

    void connectAPI(Action<GooglePlaceAPI> success, Action<String> failure) {
        GooglePlaceAPIOutletFactory
            .build(getContext())
            .connect(
                (api) -> success.run(api),
                () -> failure.run(getContext().getString(R.string.error_no_network))
            );
    }

    void run(BackgroundTask task) {
        _backgroundTasks.put(task.getId(), task);
        task.whenDone((t) -> _backgroundTasks.remove(task.getId()));
        task.execute();
    }

    void runOnMainThread(Action0 action) {
        new Handler(Looper.getMainLooper()).post(() -> action.run());
    }

    void processError(RetrofitError error, Action<String> failure) {
        Log.e(getClass().getSimpleName(), error.getMessage(), error);
        failure.run(getContext().getString(R.string.error_unknown));
    }

    @Override
    public void cancelAllTasks() {
        for (Map.Entry<String, BackgroundTask> pair : _backgroundTasks.entrySet()) {
            pair.getValue().cancel(false);
        }

        _backgroundTasks = new HashMap<>();
    }
}
