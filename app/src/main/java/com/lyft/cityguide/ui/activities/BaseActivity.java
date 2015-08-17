package com.lyft.cityguide.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;
import com.lyft.cityguide.ui.events.ConfirmationEvent;
import com.lyft.cityguide.ui.events.DoneEvent;
import com.lyft.cityguide.ui.events.ErrorEvent;
import com.lyft.cityguide.ui.events.ForkEvent;
import com.lyft.cityguide.ui.events.InfoEvent;

import java.util.Map;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * @class BaseActivity
 * @brief
 */
public abstract class BaseActivity extends Activity {
    private final static int SPINNER_DELAY_MS         = 250;
    private final static int SPINNER_DELAY_TIMEOUT_MS = 10 * 1000;

    // Custom event buses
    private static EventBus _spinnerBus;
    private final static Object _spinnerBusLock = new Object();
    private static EventBus _popupBus;
    private final static Object _popupBusLock = new Object();

    // Relative to the spinner
    private ProgressDialog _spinner;
    private int            _backgroundThreads;
    private Handler        _spinnerDelayHandler;
    private Handler        _spinnerTimeoutHandler;

    IPlaceBLL getPlaceBLL() {
        return BLLFactory.place(getApplicationContext());
    }

    void setFragment(int resourceId, Fragment fragment) {
        getFragmentManager()
            .beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .replace(resourceId, fragment)
            .addToBackStack(fragment.getClass().getSimpleName())
            .commit();
    }

    /**
     * Sets new fragments
     *
     * @param fragments
     * @param keepInStack
     */
    void setFragments(Map<Integer, Fragment> fragments, boolean keepInStack) {
        FragmentTransaction t = getFragmentManager().beginTransaction();

        t.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        for (Map.Entry<Integer, Fragment> e : fragments.entrySet()) {
            t.replace(e.getKey().intValue(), e.getValue());
        }

        if (keepInStack) {
            t.addToBackStack(null);
        }

        t.commit();
    }

    void setFragments(Map<Integer, Fragment> fragments) {
        setFragments(fragments, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _spinner = new ProgressDialog(this);
        _spinner.setIndeterminate(true);
        _spinner.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        _backgroundThreads = 0;
        getSpinnerBus().register(this);
        getPopupBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getSpinnerBus().unregister(this);
        getPopupBus().unregister(this);

        // Stop spinner
        if (_spinner != null && _spinner.isShowing()) {
            _spinner.dismiss();
        }

        // Cancel pending timers
        if (_spinnerDelayHandler != null) {
            _spinnerDelayHandler.removeCallbacksAndMessages(null);
        }
        if (_spinnerTimeoutHandler != null) {
            _spinnerTimeoutHandler.removeCallbacksAndMessages(null);
        }

        // Cancel background tasks
        getPlaceBLL().cancelAllTasks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Crouton.cancelAllCroutons();
    }

    public static EventBus getSpinnerBus() {
        if (_spinnerBus == null) {
            synchronized (_spinnerBusLock) {
                if (_spinnerBus == null) {
                    _spinnerBus = EventBus.builder()
                                          .logNoSubscriberMessages(true)
                                          .sendNoSubscriberEvent(true)
                                          .build();
                }
            }
        }

        return _spinnerBus;
    }

    public static EventBus getPopupBus() {
        if (_popupBus == null) {
            synchronized (_popupBusLock) {
                if (_popupBus == null) {
                    _popupBus = EventBus.builder()
                                        .logNoSubscriberMessages(true)
                                        .sendNoSubscriberEvent(true)
                                        .build();
                }
            }
        }

        return _popupBus;
    }

    public void onEventMainThread(ForkEvent event) {
        _backgroundThreads++;

        if (_backgroundThreads == 1) { // Run on UI thread only, so no lock
            _spinnerDelayHandler = new Handler(Looper.getMainLooper());

            // Delay showing for short operations
            _spinnerDelayHandler.postDelayed(
                () -> {
                    _spinnerDelayHandler = null;
                    _spinner.show();
                    _spinner.setContentView(R.layout.spinner); // Must be called after show()

                    // Hide it automatically after a certain timeout
                    _spinnerTimeoutHandler = new Handler(Looper.getMainLooper());
                    _spinnerTimeoutHandler.postDelayed(
                        () -> {
                            _spinnerTimeoutHandler = null;
                            _spinner.dismiss();
                            getPopupBus().post(new ErrorEvent(getString(R.string.error_unknown)));
                        },
                        SPINNER_DELAY_TIMEOUT_MS
                    );
                },
                SPINNER_DELAY_MS
            );
        }
    }

    public void onEventMainThread(DoneEvent event) {
        _backgroundThreads--;

        if (_backgroundThreads == 0) { // Run on UI thread only, so no lock
            if (_spinnerDelayHandler == null) {
                _spinner.dismiss();
                if (_spinnerTimeoutHandler != null) {
                    _spinnerTimeoutHandler.removeCallbacksAndMessages(null);
                }
            } else {
                // Still hidden
                _spinnerDelayHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    public void onEventMainThread(InfoEvent event) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, event.getMessage(), Style.INFO).show();
    }

    public void onEventMainThread(ConfirmationEvent event) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, event.getMessage(), Style.CONFIRM).show();
    }

    public void onEventMainThread(ErrorEvent event) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, event.getMessage(), Style.ALERT).show();
    }
}
