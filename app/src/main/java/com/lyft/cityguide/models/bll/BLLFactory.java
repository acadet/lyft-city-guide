package com.lyft.cityguide.models.bll;

import android.content.Context;

import com.lyft.cityguide.models.bll.interfaces.IDistanceBLL;
import com.lyft.cityguide.models.bll.interfaces.IPlaceBLL;
import com.lyft.cityguide.models.bll.interfaces.ISettingsBLL;
import com.lyft.cityguide.models.dao.DAOFactory;

/**
 * @class BLLFactory
 * @brief
 */
public class BLLFactory {
    private static IPlaceBLL _place;
    private final static Object _placeLock = new Object();

    private static IDistanceBLL _distance;
    private final static Object _distanceLock = new Object();

    private static ISettingsBLL _settings;
    private final static Object _settingsLock = new Object();

    public static IPlaceBLL place(Context context) {
        if (_place == null) {
            synchronized (_placeLock) {
                if (_place == null) {
                    _place = new PlaceBLL(context, distance(context), settings(context));
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

    public static ISettingsBLL settings(Context context) {
        if (_settings == null) {
            synchronized (_settingsLock) {
                if (_settings == null) {
                    _settings = new SettingsBLL(DAOFactory.settings(context));
                }
            }
        }

        return _settings;
    }
}
