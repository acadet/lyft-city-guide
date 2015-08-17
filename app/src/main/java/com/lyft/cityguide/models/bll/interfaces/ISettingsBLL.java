package com.lyft.cityguide.models.bll.interfaces;

import com.lyft.cityguide.models.beans.RangeSetting;

/**
 * @class ISettingsBLL
 * @brief
 */
public interface ISettingsBLL {
    RangeSetting get();

    void save(RangeSetting value);
}
