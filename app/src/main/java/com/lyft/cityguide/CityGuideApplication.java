package com.lyft.cityguide;

import android.app.Application;

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

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return instance.applicationComponent;
    }
}
