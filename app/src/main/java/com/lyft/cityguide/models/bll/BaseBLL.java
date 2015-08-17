package com.lyft.cityguide.models.bll;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.interfaces.IBLL;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * @class BaseBLL
 * @brief
 */
class BaseBLL implements IBLL {
    private Context         _context;
    private List<AsyncTask> _backgroundTasks;
    private final Object _backgroundTaskLock = new Object();

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
        _backgroundTasks = new LinkedList<>();
    }

    Context getContext() {
        return _context;
    }

    String getAPIKey() {
        return getContext().getString(R.string.google_service_api_key);
    }

    String latLngFromLocation(Location l) {
        return new Double(l.getLatitude()).toString() + ',' + new Double(l.getLongitude()).toString();
    }

    AsyncTask runInBackground(Action0 task) {
        AsyncTask asyncTask;

        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                task.run();

                return null;
            }
        };

        _backgroundTasks.add(asyncTask);
        asyncTask.execute();

        return asyncTask;
    }

    void cancel(AsyncTask task) {
        synchronized (_backgroundTaskLock) {
            for (int i = 0, s = _backgroundTasks.size(); i < s; i++) {
                if (_backgroundTasks.get(i) == task) {
                    task.cancel(true);
                    _backgroundTasks.remove(i);
                    return;
                }
            }
        }
    }

    void whenDone(AsyncTask task) {
        synchronized (_backgroundTaskLock) {
            for (int i = 0, s = _backgroundTasks.size(); i < s; i++) {
                if (_backgroundTasks.get(i) == task) {
                    _backgroundTasks.remove(i);
                    return;
                }
            }
        }
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
        if (_backgroundTasks.size() > 0) { // Do not lock if nothing to remove
            synchronized (_backgroundTaskLock) {
                if (_backgroundTasks.size() > 0) {
                    for (AsyncTask t : _backgroundTasks) {
                        t.cancel(true);
                    }
                    _backgroundTasks = new LinkedList<>();
                }
            }
        }
    }
}
