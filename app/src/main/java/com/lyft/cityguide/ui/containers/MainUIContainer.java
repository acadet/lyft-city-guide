package com.lyft.cityguide.ui.containers;

import android.content.Context;
import android.util.AttributeSet;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.scoop.UiContainer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * MainUIContainer
 * <p>
 */
public class MainUIContainer extends UiContainer {
    @Inject
    @Named("app")
    IRouter router;

    public MainUIContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        CityGuideApplication.getApplicationComponent().inject(this);
        router.observe(this::goTo);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        router.stopObserving(this::goTo);
    }
}
