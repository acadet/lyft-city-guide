package com.lyft.cityguide.dao;

import com.lyft.cityguide.domain.SearchRangeSetting;

/**
 * @class ISettingsDAO
 * @brief Saves settings
 */
public interface ISettingsDAO {
    SearchRangeSetting getSearchRange();

    void saveSearchRange(SearchRangeSetting value);
}
