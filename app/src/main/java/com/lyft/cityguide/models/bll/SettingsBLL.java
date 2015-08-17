package com.lyft.cityguide.models.bll;

import com.lyft.cityguide.models.beans.RangeSetting;
import com.lyft.cityguide.models.bll.interfaces.ISettingsBLL;
import com.lyft.cityguide.models.dao.interfaces.ISettingsDAO;

/**
 * @class SettingsBLL
 * @brief
 */
class SettingsBLL implements ISettingsBLL {
    private ISettingsDAO _dao;

    SettingsBLL(ISettingsDAO dao) {
        _dao = dao;
    }


    @Override
    public RangeSetting get() {
        return _dao.get();
    }

    @Override
    public void save(RangeSetting value) {
        _dao.save(value);
    }
}
