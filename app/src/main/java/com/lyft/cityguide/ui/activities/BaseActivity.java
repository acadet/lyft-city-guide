package com.lyft.cityguide.ui.activities;

import android.app.Activity;
import android.os.Bundle;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.routers.IRouter;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * BaseActivity
 */
public abstract class BaseActivity extends Activity {
    @Inject
    @Named("app")
    IRouter appRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CityGuideApplication.getApplicationComponent().inject(this);
    }
}
