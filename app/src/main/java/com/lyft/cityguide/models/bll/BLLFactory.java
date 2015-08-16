package com.lyft.cityguide.models.bll;

import android.content.Context;

import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;

/**
 * @class BLLFactory
 * @brief
 */
public class BLLFactory {
    private static IPlaceBLL _place;
    private final static Object _placeLock = new Object();

    public static IPlaceBLL place(Context context) {
        if (_place == null) {
            synchronized (_placeLock) {
                if (_place == null) {
                    _place = new PlaceBLL(context);
                }
            }
        }

        return _place;
    }
}
