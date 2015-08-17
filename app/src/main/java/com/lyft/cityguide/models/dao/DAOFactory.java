package com.lyft.cityguide.models.dao;

import android.content.Context;

import com.lyft.cityguide.models.dao.interfaces.ISettingsDAO;

/**
 * @class DAOFactory
 * @brief
 */
public class DAOFactory {
    private static ISettingsDAO _settings;
    private final static Object _settingsLock = new Object();

    public static ISettingsDAO settings(Context context) {
        if (_settings == null) {
            synchronized (_settingsLock) {
                if (_settings == null) {
                    _settings = new SettingsDAO(context);
                }
            }
        }

        return _settings;
    }
}
