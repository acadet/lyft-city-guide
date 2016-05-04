package com.lyft.cityguide.ui;

import com.lyft.cityguide.ui.controllers.spinner.SpinnerController;

import javax.inject.Singleton;

import dagger.Subcomponent;

/**
 * UIComponent
 * <p>
 */
@Singleton
@Subcomponent(modules = {
    UIModule.class
})
public interface UIComponent {
    void inject(SpinnerController spinnerController);
}
