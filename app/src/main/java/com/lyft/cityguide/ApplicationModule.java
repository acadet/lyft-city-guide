package com.lyft.cityguide;

import android.app.Application;
import android.content.Context;

import com.lyft.cityguide.ui.components.Spinner;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ApplicationModule
 * <p>
 */
@Module
public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public Spinner provideSpinner(Context context) {
        return new Spinner(context);
    }
}
