package com.lyft.cityguide.models.bll.api;

import android.content.Context;

import com.lyft.cityguide.models.bll.api.interfaces.IGooglePlaceAPIOutlet;

/**
 * @class GooglePlaceAPIOutletFactory
 * @brief
 */
public class GooglePlaceAPIOutletFactory {
    private static IGooglePlaceAPIOutlet _outlet;
    private final static Object _lock = new Object();

    public static IGooglePlaceAPIOutlet build(Context context) {
        if (_outlet == null) {
            synchronized (_lock) {
                if (_outlet == null) {
                    _outlet = new GooglePlaceAPIOutlet(context);
                }
            }
        }

        return _outlet;
    }
}
