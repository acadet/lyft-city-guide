package com.lyft.cityguide.models.bll.interfaces;

import android.location.Location;

import com.lyft.cityguide.models.beans.Distance;
import com.lyft.cityguide.models.beans.Place;
import com.lyft.cityguide.utils.actions.Action;

import java.util.List;

/**
 * @class IDistanceBLL
 * @brief
 */
public interface IDistanceBLL {
    void getDistances(Location currentLocation, List<Place> places,
                      Action<List<Distance>> success, Action<String> failure);
}
