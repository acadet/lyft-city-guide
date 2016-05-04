package com.lyft.cityguide.ui;

import android.app.Activity;

import com.lyft.cityguide.ui.components.Spinner;

import dagger.Module;
import dagger.Provides;

/**
 * UIModule
 * <p>
 */
@Module
public class UIModule {
    private Activity currentActivity;

    public UIModule(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    @Provides
    public Spinner provideSpinner() {
        return new Spinner(currentActivity);
    }
}
