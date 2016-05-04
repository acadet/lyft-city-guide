package com.lyft.cityguide.dao;

import android.content.SharedPreferences;

import com.lyft.cityguide.domain.SearchRangeSetting;

/**
 * @class SettingsDAO
 * @brief
 */
class SettingsDAO implements ISettingsDAO {
    private static final String RANGE_KEY = "range";

    private final SharedPreferences         dal;
    private final ISearchRangeSettingMapper mapper;

    SettingsDAO(SharedPreferences dal, ISearchRangeSettingMapper mapper) {
        this.dal = dal;
        this.mapper = mapper;
    }

    @Override
    public SearchRangeSetting getSearchRange() {
        return mapper.map(
            SearchRangeSettingDTO.fromInt(
                dal.getInt(RANGE_KEY, SearchRangeSettingDTO.ONE_MILE.toInt())
            )
        );
    }

    @Override
    public void saveSearchRange(SearchRangeSetting value) {
        dal
            .edit()
            .putInt(RANGE_KEY, mapper.map(value).toInt())
            .commit();
    }
}
