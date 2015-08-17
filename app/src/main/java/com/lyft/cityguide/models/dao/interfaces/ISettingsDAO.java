package com.lyft.cityguide.models.dao.interfaces;

import com.lyft.cityguide.models.beans.RangeSetting;

/**
 * @class ISettingsDAO
 * @brief
 */
public interface ISettingsDAO {
    RangeSetting get();

    void save(RangeSetting value);
}
