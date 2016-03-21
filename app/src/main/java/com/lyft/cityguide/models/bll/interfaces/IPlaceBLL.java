package com.lyft.cityguide.models.bll.interfaces;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.utils.actions.Action;

import java.util.List;

/**
 * @class IPlaceBLL
 * @brief Provides all the operations relative to places
 */
public interface IPlaceBLL extends IBLL {
    void getBarsAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);

    void moreBarsAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);

    void getBistrosAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);

    void moreBistrosAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);

    void getCafesAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);

    void moreCafesAround(Action<List<PointOfInterestBLLDTO>> success, Action<String> failure);
}
