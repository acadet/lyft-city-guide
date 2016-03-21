package com.lyft.cityguide.models.dao;

import com.lyft.cityguide.models.dao.dto.SearchRangeSettingDAODTO;

/**
 * @class ISettingsDAO
 * @brief Saves settings
 */
public interface ISettingsDAO {
    SearchRangeSettingDAODTO getSearchRange();

    void saveSearchRange(SearchRangeSettingDAODTO value);
}
