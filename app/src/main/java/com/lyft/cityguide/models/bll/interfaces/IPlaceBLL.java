package com.lyft.cityguide.models.bll.interfaces;

import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.utils.actions.Action;

import java.util.List;

/**
 * @class IPlaceBLL
 * @brief
 */
public interface IPlaceBLL {
    void getBarsAround(Action<List<Place>> success, Action<String> failure);
}
