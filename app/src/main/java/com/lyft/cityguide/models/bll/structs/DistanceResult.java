package com.lyft.cityguide.models.bll.structs;

import com.lyft.cityguide.models.beans.Distance;

import java.util.ArrayList;
import java.util.List;

/**
 * @class DistanceResult
 * @brief
 */
public class DistanceResult {
    private List<Distance> _distances;

    public DistanceResult() {
        _distances = new ArrayList<>();
    }

    public List<Distance> getDistances() {
        return _distances;
    }

    public void addDistance(Distance distance) {
        _distances.add(distance);
    }
}
