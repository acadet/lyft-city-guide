package com.lyft.cityguide.models.bll.utils;

import android.os.AsyncTask;

import com.lyft.cityguide.utils.actions.Action;

import java.util.UUID;

/**
 * @class BackgroundTask
 * @brief
 */
public abstract class BackgroundTask<T> extends AsyncTask<Void, Void, T> {
    private String                 _id;
    private Action<BackgroundTask> _whenDone;

    public BackgroundTask() {
        _id = UUID.randomUUID().toString();
    }

    public String getId() {
        return _id;
    }

    public void whenDone(Action<BackgroundTask> callback) {
        _whenDone = callback;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);

        _whenDone.run(this);
    }
}
