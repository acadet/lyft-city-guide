package com.lyft.cityguide.ui.components;

import android.content.Context;
import android.util.AttributeSet;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.routers.AppRouter;
import com.lyft.scoop.UiContainer;

import javax.inject.Inject;

/**
 * MainUIContainer
 * <p>
 */
public class MainUIContainer extends UiContainer {
    @Inject
    AppRouter appRouter;

    public MainUIContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        CityGuideApplication.getApplicationComponent().inject(this);
        appRouter.observe(this::goTo);
    }
}
