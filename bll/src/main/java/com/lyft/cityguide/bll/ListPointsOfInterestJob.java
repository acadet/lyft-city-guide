package com.lyft.cityguide.bll;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;
import com.lyft.cityguide.services.google.distancematrix.IGoogleDistanceMatrixService;
import com.lyft.cityguide.services.google.place.GooglePlaceErrors;
import com.lyft.cityguide.services.google.place.IGooglePlaceService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * ListPointsOfInterestJob
 * <p>
 */
class ListPointsOfInterestJob extends BLLJob {
    private static class Holder {
        List<PointOfInterest> pointOfInterestList;
    }

    private final IGooglePlaceService          googlePlaceService;
    private final IGoogleDistanceMatrixService googleDistanceMatrixService;
    private       Location                     latestKnownLocation;

    ListPointsOfInterestJob(IGooglePlaceService googlePlaceService, IGoogleDistanceMatrixService googleDistanceMatrixService) {
        this.googlePlaceService = googlePlaceService;
        this.googleDistanceMatrixService = googleDistanceMatrixService;
    }

    private Observable<List<PointOfInterest>> create(
        boolean isAskingForMore,
        Location currentLocation,
        SearchRangeSetting searchRangeSetting,
        PointOfInterest.Kind placeType
    ) {
        return Observable
            .create(new Observable.OnSubscribe<List<PointOfInterest>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterest>> subscriber) {
                    final Holder holder = new Holder();
                    final Subscriber<Void> distanceMatrixSubscriber = new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onNext(holder.pointOfInterestList);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            handleError(e, subscriber);
                        }

                        @Override
                        public void onNext(Void aVoid) {
                        }
                    };
                    final Subscriber<List<PointOfInterest>> placeSubscriber = new Subscriber<List<PointOfInterest>>() {
                        @Override
                        public void onCompleted() {
                            googleDistanceMatrixService
                                .fetchDistances(currentLocation, holder.pointOfInterestList)
                                .observeOn(Schedulers.immediate())
                                .subscribe(distanceMatrixSubscriber);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof GooglePlaceErrors.NoMoreResult) {
                                subscriber.onError(new BLLErrors.NoMorePOI());
                            } else {
                                handleError(e, subscriber);
                            }
                        }

                        @Override
                        public void onNext(List<PointOfInterest> pointOfInterestBLLDTOs) {
                            holder.pointOfInterestList = pointOfInterestBLLDTOs;
                        }
                    };
                    Observable<List<PointOfInterest>> placeObservable;

                    if (isAskingForMore) {
                        placeObservable = googlePlaceService.more();
                    } else {
                        placeObservable = googlePlaceService.search(currentLocation, searchRangeSetting, placeType);
                    }

                    placeObservable.observeOn(Schedulers.immediate()).subscribe(placeSubscriber);
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    Observable<List<PointOfInterest>> list(Location currentLocation,
                                           SearchRangeSetting searchRangeSetting,
                                           PointOfInterest.Kind placeType) {
        latestKnownLocation = currentLocation;
        return create(false, currentLocation, searchRangeSetting, placeType);
    }

    Observable<List<PointOfInterest>> more() {
        return create(true, latestKnownLocation, null, null);
    }
}
