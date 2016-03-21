package com.lyft.cityguide.models.bll.interfaces;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.DistanceBLLDTO;
import com.lyft.cityguide.models.beans.PlaceBLLDTO;
import com.lyft.cityguide.utils.actions.Action;

import java.util.List;

/**
 * @class IDistanceBLL
 * @brief
 */
public interface IDistanceBLL extends IBLL {
    /**
     * Returns distances between current location and provided places
     *
     * @param currentLocation
     * @param places
     * @param success
     * @param failure
     */
    void getDistances(Location currentLocation, List<PlaceBLLDTO> places,
                      Action<List<DistanceBLLDTO>> success, Action<String> failure);
}
