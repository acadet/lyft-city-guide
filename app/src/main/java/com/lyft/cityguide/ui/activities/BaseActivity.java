package com.lyft.cityguide.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.components.Spinner;
import com.lyft.cityguide.ui.events.PopupEvents;
import com.lyft.cityguide.ui.events.SpinnerEvents;
import com.lyft.cityguide.ui.routers.AppRouter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends Activity {
    private Spinner spinner;

    @Inject
    AppRouter appRouter;

    @Inject
    @Named("popup")
    EventBus popupBus;

    @Inject
    @Named("spinner")
    EventBus spinnerBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CityGuideApplication.getApplicationComponent().inject(this);

        spinner = new Spinner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        popupBus.register(this);
        spinnerBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        popupBus.unregister(this);
        spinnerBus.unregister(this);

        Crouton.cancelAllCroutons();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfirmPopup(PopupEvents.Confirm e) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, e.message, Style.CONFIRM).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoPopup(PopupEvents.Info e) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, e.message, Style.INFO).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertPopup(PopupEvents.Alert e) {
        Crouton.cancelAllCroutons();
        Crouton.makeText(this, e.message, Style.ALERT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHidePopup(PopupEvents.Hide e) {
        Crouton.clearCroutonsForActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerShow(SpinnerEvents.Show e) {
        spinner.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerShowImmediately(SpinnerEvents.ShowImmediately e) {
        spinner.show(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpinnerHide(SpinnerEvents.Hide e) {
        spinner.hide();
    }
}
