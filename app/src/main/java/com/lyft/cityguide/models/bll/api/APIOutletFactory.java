package com.lyft.cityguide.models.bll.api;

import android.content.Context;

import com.lyft.cityguide.models.bll.api.interfaces.IGoogleDistanceMatrixAPIOutlet;
import com.lyft.cityguide.models.bll.api.interfaces.IGooglePlaceAPIOutlet;

/**
 * @class APIOutletFactory
 * @brief
 */
public class APIOutletFactory {
    private final static Object _googlePlaceLock = new Object();
    private static IGooglePlaceAPIOutlet _googlePlace;

    private final static Object _googleDistanceMatrixLock = new Object();
    private static IGoogleDistanceMatrixAPIOutlet _googleDistanceMatrix;

    public static IGooglePlaceAPIOutlet googlePlace(Context context) {
        if (_googlePlace == null) {
            synchronized (_googlePlaceLock) {
                if (_googlePlace == null) {
                    _googlePlace = new GooglePlaceAPIOutlet(context);
                }
            }
        }

        return _googlePlace;
    }

    public static IGoogleDistanceMatrixAPIOutlet googleDistanceMatrix(Context context) {
        if (_googleDistanceMatrix == null) {
            synchronized (_googleDistanceMatrixLock) {
                if (_googleDistanceMatrix == null) {
                    _googleDistanceMatrix = new GoogleDistanceMatrixAPIOutlet(context);
                }
            }
        }

        return _googleDistanceMatrix;
    }
}
