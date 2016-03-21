package com.lyft.cityguide.ui.routers;

import com.lyft.scoop.RouteChange;

/**
 * IRouterObserver
 * <p>
 */
public interface IRouterObserver {
    void onScoopChanged(RouteChange routeChange);
}
