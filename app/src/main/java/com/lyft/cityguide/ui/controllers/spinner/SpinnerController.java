package com.lyft.cityguide.ui.controllers.spinner;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.components.Spinner;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.cityguide.ui.screens.spinner.ShowSpinnerScreen;
import com.lyft.scoop.Screen;
import com.lyft.scoop.ViewController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * SpinnerController
 * <p>
 */
public class SpinnerController extends ViewController {

    @Inject
    Spinner spinner;

    @Inject
    @Named("spinner")
    IRouter router;

    @Override
    protected int layoutId() {
        return R.layout.empty;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        BaseActivity.getUIComponent().inject(this);

        spinner.setOnTimeoutObserver(() -> router.goBack());

        ShowSpinnerScreen screen = Screen.fromController(this);

        if (screen.isInstant) {
            spinner.show(false);
        } else {
            spinner.show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        spinner.hide();
        spinner.removeOnTimeoutObserver();
    }
}
