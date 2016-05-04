package com.lyft.cityguide.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.UIComponent;
import com.lyft.cityguide.ui.UIModule;
import com.lyft.cityguide.ui.routers.IRouter;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends Activity {

    private static BaseActivity instance;

    private UIComponent uiComponent;

    @Inject
    @Named("app")
    IRouter appRouter;

    @Inject
    @Named("toast")
    IRouter toastRouter;

    @Inject
    @Named("spinner")
    IRouter spinnerRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        uiComponent = CityGuideApplication
            .getApplicationComponent()
            .uiComponent(new UIModule(this));

        CityGuideApplication.getApplicationComponent().inject(this);
    }

    public static UIComponent getUIComponent() {
        return instance.uiComponent;
    }
}
