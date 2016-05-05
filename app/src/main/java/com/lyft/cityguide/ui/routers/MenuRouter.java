package com.lyft.cityguide.ui.routers;

import com.lyft.cityguide.ui.screens.menu.InitMenuScreen;
import com.lyft.scoop.ScreenScooper;

/**
 * MenuRouter
 * <p>
 */
class MenuRouter extends BaseRouter {
    private int backStackSize;

    MenuRouter(ScreenScooper screenScooper) {
        super(screenScooper);
        backStackSize = 0;

        observe((routeChange) -> {
            if (routeChange.previous instanceof InitMenuScreen) {
                backStackSize++;
            } else if (routeChange.next instanceof InitMenuScreen && routeChange.previous != null) {
                backStackSize--;
            }
        });
    }

    @Override
    public boolean hasActiveScreen() {
        return backStackSize > 0;
    }
}
