package com.lyft.cityguide.models.bll.api.interfaces;

import com.lyft.cityguide.models.bll.api.GoogleDistanceMatrixAPI;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

/**
 * @class IGoogleDistanceMatrixAPIOutlet
 * @brief
 */
public interface IGoogleDistanceMatrixAPIOutlet {
    void connect(Action<GoogleDistanceMatrixAPI> success, Action0 failure);
}
