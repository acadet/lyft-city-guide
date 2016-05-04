package com.lyft.cityguide.ui.routers;

import com.lyft.scoop.RouteChange;

/**
 * IRouterScoopChangedObserver
 * <p>
 */
public interface IRouterScoopChangedObserver {
    void onScoopChanged(RouteChange routeChange);
}
