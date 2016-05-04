package com.lyft.cityguide.ui.routers;

import com.lyft.cityguide.ui.screens.toast.InitToastScreen;
import com.lyft.scoop.ScreenScooper;

/**
 * ToastRouter
 * <p>
 */
class ToastRouter extends BaseRouter {
    ToastRouter(ScreenScooper screenScooper) {
        super(screenScooper);

        resetTo(new InitToastScreen());
    }
}
