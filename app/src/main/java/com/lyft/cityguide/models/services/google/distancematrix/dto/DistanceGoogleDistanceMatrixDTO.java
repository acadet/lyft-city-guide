package com.lyft.cityguide.models.services.google.distancematrix.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DistanceGoogleDistanceMatrixDTO
 * <p>
 */
public class DistanceGoogleDistanceMatrixDTO {
    private List<Float> distances;

    public DistanceGoogleDistanceMatrixDTO() {
        this.distances = new ArrayList<>();
    }

    public boolean hasDistances() {
        return distances.size() > 0;
    }

    public List<Float> getDistances() {
        return distances;
    }

    public DistanceGoogleDistanceMatrixDTO addDistance(Float distance) {
        distances.add(distance);
        return this;
    }
}
