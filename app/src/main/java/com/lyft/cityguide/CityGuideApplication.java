package com.lyft.cityguide;

import android.app.Application;
import android.view.View;

import com.lyft.scoop.Scoop;
import com.lyft.scoop.ViewBinder;

import butterknife.ButterKnife;
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

        Scoop.setViewBinder(new ViewBinder() {
            @Override
            public void bind(Object object, View view) {
                ButterKnife.bind(object, view);
            }

            @Override
            public void unbind(Object object) {
                ButterKnife.unbind(object);
            }
        });

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return instance.applicationComponent;
    }
}
