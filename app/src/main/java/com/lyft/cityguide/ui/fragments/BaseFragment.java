package com.lyft.cityguide.ui.fragments;

import android.app.Fragment;

import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;
import com.lyft.cityguide.models.bll.interfaces.ISettingsBLL;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.events.ConfirmationEvent;
import com.lyft.cityguide.ui.events.DoneEvent;
import com.lyft.cityguide.ui.events.ErrorEvent;
import com.lyft.cityguide.ui.events.ForkEvent;
import com.lyft.cityguide.ui.events.InfoEvent;

import de.greenrobot.event.EventBus;

/**
 * @class BaseFragment
 * @brief
 */
public class BaseFragment extends Fragment {
    private static EventBus _resultListBus;
    private final static Object _resultListBusLock = new Object();

    private static EventBus _menuBus;
    private final static Object _menuBusLock = new Object();

    IPlaceBLL getPlaceBLL() {
        return BLLFactory.place(getActivity());
    }

    ISettingsBLL getSettingsBLL() {
        return BLLFactory.settings(getActivity());
    }

    void inform(String message) {
        BaseActivity.getPopupBus().post(new InfoEvent(message));
    }

    void confirm(String message) {
        BaseActivity.getPopupBus().post(new ConfirmationEvent(message));
    }

    void warn(String message) {
        BaseActivity.getPopupBus().post(new ErrorEvent(message));
    }

    void fork() {
        BaseActivity.getSpinnerBus().post(new ForkEvent());
    }

    void done() {
        BaseActivity.getSpinnerBus().post(new DoneEvent());
    }

    void onError(String message) {
        done();
        warn(message);
    }

    static EventBus getResultListBus() {
        if (_resultListBus == null) {
            synchronized (_resultListBusLock) {
                if (_resultListBus == null) {
                    _resultListBus = EventBus.builder()
                                             .logNoSubscriberMessages(true)
                                             .sendNoSubscriberEvent(true)
                                             .build();
                }
            }
        }

        return _resultListBus;
    }

    public static EventBus getMenuBus() {
        if (_menuBus == null) {
            synchronized (_menuBusLock) {
                if (_menuBus == null) {
                    _menuBus = EventBus.builder()
                                       .logNoSubscriberMessages(true)
                                       .sendNoSubscriberEvent(true)
                                       .build();
                }
            }
        }

        return _menuBus;
    }
}
