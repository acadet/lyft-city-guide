package com.lyft.cityguide.bll;

import com.lyft.cityguide.domain.PointOfInterest;

import java.util.List;

import rx.Observable;

/**
 * IPointOfInterestBLL
 * <p>
 */
public interface IPointOfInterestBLL {
    Observable<List<PointOfInterest>> listAround(PointOfInterest.Kind kind);

    Observable<List<PointOfInterest>> listMore();
}
