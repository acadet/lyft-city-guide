package com.lyft.cityguide.ui.containers;

import android.content.Context;
import android.util.AttributeSet;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.scoop.UiContainer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * SpinnerUIContainer
 * <p>
 */
public class SpinnerUIContainer extends UiContainer {

    @Inject
    @Named("spinner")
    IRouter router;

    public SpinnerUIContainer(Context context, AttributeSet attrs) {
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
