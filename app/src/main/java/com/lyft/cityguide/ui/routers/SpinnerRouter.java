package com.lyft.cityguide.ui.routers;

import com.lyft.cityguide.ui.screens.spinner.InitSpinnerScreen;
import com.lyft.scoop.ScreenScooper;

/**
 * SpinnerRouter
 * <p>
 */
class SpinnerRouter extends BaseRouter {
    SpinnerRouter(ScreenScooper screenScooper) {
        super(screenScooper);
        resetTo(new InitSpinnerScreen());
    }
}
