package com.lyft.cityguide.models.bll;

import android.content.Context;

import com.lyft.cityguide.models.bll.interfaces.IDistanceBLL;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;

/**
 * @class BLLFactory
 * @brief
 */
public class BLLFactory {
    private static IPlaceBLL _place;
    private final static Object _placeLock = new Object();

    private static IDistanceBLL _distance;
    private final static Object _distanceLock = new Object();

    public static IPlaceBLL place(Context context) {
        if (_place == null) {
            synchronized (_placeLock) {
                if (_place == null) {
                    _place = new PlaceBLL(context, distance(context));
                }
            }
        }

        return _place;
    }

    public static IDistanceBLL distance(Context context) {
        if (_distance == null) {
            synchronized (_distanceLock) {
                if (_distance == null) {
                    _distance = new DistanceBLL(context);
                }
            }
        }

        return _distance;
    }
}
