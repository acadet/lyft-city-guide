package com.lyft.cityguide.ui.controllers;

import android.content.Context;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.R;
import com.lyft.cityguide.bll.BLLErrors;
import com.lyft.cityguide.bll.IPointOfInterestBLL;
import com.lyft.cityguide.bll.ISearchSettingBLL;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.cityguide.ui.screens.spinner.ShowSpinnerScreen;
import com.lyft.cityguide.ui.screens.toast.AlertScreen;
import com.lyft.cityguide.ui.screens.toast.ConfirmScreen;
import com.lyft.cityguide.ui.screens.toast.InformScreen;
import com.lyft.scoop.ViewController;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Subscriber;
import timber.log.Timber;

/**
 * BaseController
 * <p>
 */
public abstract class BaseController extends ViewController {

    abstract class BaseSubscriber<T> extends Subscriber<T> {
        @Override
        public void onError(Throwable e) {
            if (e instanceof BLLErrors.NoConnection) {
                inform(context.getString(R.string.no_connection_error));
            } else if (e instanceof BLLErrors.ServiceError) {
                inform(context.getString(R.string.internal_server_error));
            } else {
                Timber.e(e, "Unhandled throwable in BaseSubscriber");
                alert(e.getMessage());
            }
        }
    }

    @Inject
    Context context;

    @Inject
    @Named("app")
    IRouter appRouter;

    @Inject
    @Named("toast")
    IRouter toastRouter;

    @Inject
    @Named("spinner")
    IRouter spinnerRouter;

    @Inject
    @Named("menu")
    IRouter menuRouter;

    @Inject
    IPointOfInterestBLL pointOfInterestBLL;

    @Inject
    ISearchSettingBLL searchSettingBLL;

    @Override
    public void onAttach() {
        super.onAttach();
        CityGuideApplication.getApplicationComponent().inject(this);
    }

    public void inform(String message) {
        toastRouter.goTo(new InformScreen(message));
    }

    public void confirm(String message) {
        toastRouter.goTo(new ConfirmScreen(message));
    }

    public void alert(String message) {
        toastRouter.goTo(new AlertScreen(message));
    }

    public void showSpinner() {
        spinnerRouter.goTo(new ShowSpinnerScreen());
    }

    public void showSpinnerImmediately() {
        spinnerRouter.goTo(new ShowSpinnerScreen(true));
    }

    public void hideSpinner() {
        spinnerRouter.goBack();
    }
}
