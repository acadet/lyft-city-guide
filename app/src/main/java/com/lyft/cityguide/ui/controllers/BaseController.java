package com.lyft.cityguide.ui.controllers;

import android.content.Context;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.BLLErrors;
import com.lyft.cityguide.models.bll.IDataReadingBLL;
import com.lyft.cityguide.models.bll.IDataWritingBLL;
import com.lyft.cityguide.ui.events.PopupEvents;
import com.lyft.cityguide.ui.events.SpinnerEvents;
import com.lyft.scoop.ViewController;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * BaseController
 * <p>
 */
public abstract class BaseController extends ViewController {

    public abstract class BaseSubscriber<T> extends Subscriber<T> {
        @Override
        public void onError(Throwable e) {
            if (e instanceof BLLErrors.NoConnection) {
                inform(context.getString(R.string.no_connection_error));
            } else if (e instanceof BLLErrors.ServiceError) {
                inform(context.getString(R.string.internal_server_error));
            } else {
                alert(e.getMessage());
            }
        }
    }

    @Inject
    Context context;

    @Inject
    @Named("popup")
    EventBus popupBus;

    @Inject
    @Named("spinner")
    EventBus spinnerBus;

    @Inject
    IDataReadingBLL dataReadingBLL;

    @Inject
    IDataWritingBLL dataWritingBLL;

    @Override
    public void onAttach() {
        super.onAttach();
        CityGuideApplication.getApplicationComponent().inject(this);
        ButterKnife.bind(this, getView());
    }

    public void inform(String message) {
        popupBus.post(new PopupEvents.Info(message));
    }

    public void confirm(String message) {
        popupBus.post(new PopupEvents.Confirm(message));
    }

    public void alert(String message) {
        popupBus.post(new PopupEvents.Alert(message));
    }

    public void hideNotification() {
        popupBus.post(new PopupEvents.Hide());
    }

    public void showSpinner() {
        spinnerBus.post(new SpinnerEvents.Show());
    }

    public void hideSpinner() {
        spinnerBus.post(new SpinnerEvents.Hide());
    }
}
