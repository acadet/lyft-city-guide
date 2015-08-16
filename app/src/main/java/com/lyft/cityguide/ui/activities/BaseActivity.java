package com.lyft.cityguide.ui.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

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
    private static EventBus _spinnerBus;
    private final static Object _spinnerBusLock = new Object();
    private static EventBus _popupBus;
    private final static Object _popupBusLock = new Object();

    private ProgressDialog _spinner;
    private int            _backgroundThreads;
    private Handler        _backgroundHandler;

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

        getSpinnerBus().register(this);
        getPopupBus().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        _backgroundThreads = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();

        getSpinnerBus().unregister(this);
        getPopupBus().unregister(this);

        if (_spinner != null && _spinner.isShowing()) {
            _spinner.dismiss();
        }

        if (_backgroundHandler != null) {
            _backgroundHandler.removeCallbacksAndMessages(null);
        }

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

        if (_backgroundThreads == 1) {
            _backgroundHandler = new Handler();
            // Delay showing
            _backgroundHandler.postDelayed(
                () -> {
                    _backgroundHandler = null;
                    _spinner.show();
                    _spinner.setContentView(R.layout.spinner); // Must be called after show()
                },
                300
            );
        }
    }

    public void onEventMainThread(DoneEvent event) {
        _backgroundThreads--;

        if (_backgroundThreads == 0) {
            if (_backgroundHandler == null) {
                _spinner.dismiss();
            } else {
                _backgroundHandler.removeCallbacksAndMessages(null);
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
