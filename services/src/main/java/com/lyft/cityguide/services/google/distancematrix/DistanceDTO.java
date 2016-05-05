package com.lyft.cityguide.services.google.distancematrix;

import java.util.ArrayList;
import java.util.List;

/**
 * DistanceDTO
 * <p>
 */
class DistanceDTO {
    private List<Float> distances;

    public DistanceDTO() {
        this.distances = new ArrayList<>();
    }

    public boolean hasDistances() {
        return distances.size() > 0;
    }

    public List<Float> getDistances() {
        return distances;
    }

    public DistanceDTO addDistance(Float distance) {
        distances.add(distance);
        return this;
    }
}
