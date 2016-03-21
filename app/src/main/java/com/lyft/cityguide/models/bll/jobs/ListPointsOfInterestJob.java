package com.lyft.cityguide.models.bll.jobs;

import android.location.Location;

import com.lyft.cityguide.models.bll.BLLErrors;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.models.services.google.distancematrix.IGoogleDistanceMatrixService;
import com.lyft.cityguide.models.services.google.place.GooglePlaceErrors;
import com.lyft.cityguide.models.services.google.place.IGooglePlaceService;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * ListPointsOfInterestJob
 * <p>
 */
public class ListPointsOfInterestJob extends BLLJob {
    private Observable<List<PointOfInterestBLLDTO>> observable;

    private List<PointOfInterestBLLDTO> listToReturn;

    private boolean                  isAskingForMore;
    private Location                 currentLocation;
    private SearchRangeSettingBLLDTO searchRangeSetting;
    private PlaceType                placeType;

    ListPointsOfInterestJob(IGooglePlaceService placeService, IGoogleDistanceMatrixService distanceMatrixService) {
        observable = Observable
            .create(new Observable.OnSubscribe<List<PointOfInterestBLLDTO>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterestBLLDTO>> subscriber) {
                    Subscriber<Void> distanceMatrixSubscriber = new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onNext(listToReturn);
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

                    Subscriber<List<PointOfInterestBLLDTO>> placeSubscriber = new Subscriber<List<PointOfInterestBLLDTO>>() {
                        @Override
                        public void onCompleted() {
                            distanceMatrixService
                                .fetchDistances(currentLocation, listToReturn)
                                .observeOn(Schedulers.newThread())
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
                        public void onNext(List<PointOfInterestBLLDTO> pointOfInterestBLLDTOs) {
                            listToReturn = pointOfInterestBLLDTOs;
                        }
                    };

                    if (isAskingForMore) {
                        placeService
                            .more()
                            .observeOn(Schedulers.newThread())
                            .subscribe(placeSubscriber);
                    } else {
                        placeService
                            .search(currentLocation, searchRangeSetting, placeType)
                            .observeOn(Schedulers.newThread())
                            .subscribe(placeSubscriber);
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<List<PointOfInterestBLLDTO>> get(
        boolean isAskingForMore,
        Location currentLocation,
        SearchRangeSettingBLLDTO searchRangeSetting,
        PlaceType placeType
    ) {
        this.isAskingForMore = isAskingForMore;
        this.currentLocation = currentLocation;
        this.searchRangeSetting = searchRangeSetting;
        this.placeType = placeType;

        return observable;
    }
}
