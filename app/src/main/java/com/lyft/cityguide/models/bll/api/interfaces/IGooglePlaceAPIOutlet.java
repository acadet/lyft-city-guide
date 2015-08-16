package com.lyft.cityguide.models.bll.api.interfaces;

import com.lyft.cityguide.models.bll.api.GooglePlaceAPI;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

/**
 * @class IGooglePlaceAPIOutlet
 * @brief
 */
public interface IGooglePlaceAPIOutlet {
    void connect(Action<GooglePlaceAPI> success, Action0 failure);
}
