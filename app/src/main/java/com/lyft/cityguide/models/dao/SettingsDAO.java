package com.lyft.cityguide.models.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.lyft.cityguide.models.dao.dto.SearchRangeSettingDAODTO;

/**
 * @class SettingsDAO
 * @brief
 */
class SettingsDAO implements ISettingsDAO {
    private static final String FILE_NAME = "settings";
    private static final String RANGE_KEY = "range";

    private SharedPreferences dal;

    SettingsDAO(Context context) {
        this.dal = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public SearchRangeSettingDAODTO getSearchRange() {
        return SearchRangeSettingDAODTO.fromInt(dal.getInt(RANGE_KEY, SearchRangeSettingDAODTO.ONE_MILE.toInt()));
    }

    @Override
    public void saveSearchRange(SearchRangeSettingDAODTO value) {
        dal
            .edit()
            .putInt(RANGE_KEY, value.toInt())
            .commit();
    }
}
