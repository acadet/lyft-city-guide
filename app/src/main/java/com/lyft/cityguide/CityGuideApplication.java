package com.lyft.cityguide;

import android.app.Application;

import timber.log.Timber;

/**
 * CityGuideApplication
 * <p>
 */
public class CityGuideApplication extends Application {
    private static CityGuideApplication instance;
    private        ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Timber.plant(new Timber.DebugTree());

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return instance.applicationComponent;
    }
}
