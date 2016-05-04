package com.lyft.cityguide.ui.components;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.lyft.cityguide.R;

import rx.functions.Action0;

/**
 * Spinner
 * <p>
 */
public class Spinner {
    private final ProgressDialog progressDialog;
    private       Handler        delayedShowingHandler;
    private       boolean        delayedShowingHasBeenCancelled;
    private       Handler        timeoutHandler;
    private       boolean        timeoutHasBeenCancelled;
    private       boolean        isShowing;
    private       Action0        onTimeoutObserver;

    public Spinner(Context context) {
        progressDialog = new ProgressDialog(context, R.style.ProgressBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }

    public void show(boolean withDelay) {
        Runnable runnable;

        if (isShowing) {
            return;
        }

        runnable = () -> {
            if (delayedShowingHasBeenCancelled) {
                return;
            }

            progressDialog.show();
            progressDialog.setContentView(R.layout.spinner);
        };

        delayedShowingHasBeenCancelled = false;
        timeoutHasBeenCancelled = false;
        isShowing = true;

        if (withDelay) {
            delayedShowingHandler = new Handler(Looper.getMainLooper());
            delayedShowingHandler.postDelayed(runnable, 500);
        } else {
            new Handler().post(() -> runnable.run());
        }

        timeoutHandler = new Handler(Looper.getMainLooper());
        timeoutHandler.postDelayed(
            () -> {
                if (timeoutHasBeenCancelled) {
                    return;
                } else {
                    progressDialog.dismiss();
                    if (onTimeoutObserver != null) {
                        onTimeoutObserver.call();
                    }
                }
            },
            10 * 1000
        );
    }

    public void show() {
        show(true);
    }

    public void hide() {
        if (delayedShowingHandler != null) {
            delayedShowingHandler.removeCallbacks(null);
            delayedShowingHasBeenCancelled = true;
            delayedShowingHandler = null;
        }

        progressDialog.dismiss();
        isShowing = false;

        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacks(null);
            timeoutHasBeenCancelled = true;
            timeoutHandler = null;
        }
    }

    public void setOnTimeoutObserver(Action0 action) {
        onTimeoutObserver = action;
    }

    public void removeOnTimeoutObserver() {
        onTimeoutObserver = null;
    }
}
