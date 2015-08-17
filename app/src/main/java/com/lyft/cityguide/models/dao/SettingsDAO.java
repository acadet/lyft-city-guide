package com.lyft.cityguide.models.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.lyft.cityguide.models.beans.RangeSetting;
import com.lyft.cityguide.models.dao.interfaces.ISettingsDAO;

/**
 * @class SettingsDAO
 * @brief
 */
class SettingsDAO implements ISettingsDAO {
    private final static String FILE_NAME = "settings";
    private final static String RANGE_KEY = "range";

    SharedPreferences _dal;

    SettingsDAO(Context context) {
        _dal = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public RangeSetting get() {
        return RangeSetting.fromInt(_dal.getInt(RANGE_KEY, 0));
    }

    @Override
    public void save(RangeSetting value) {
        _dal
            .edit()
            .putInt(RANGE_KEY, value.toInt())
            .commit();
    }
}
