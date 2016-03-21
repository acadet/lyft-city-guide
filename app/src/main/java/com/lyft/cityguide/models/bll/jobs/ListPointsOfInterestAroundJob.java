package com.lyft.cityguide.models.bll.jobs;

import android.location.Location;

import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.structs.PlaceType;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * ListPointsOfInterestAroundJob
 * <p>
 */
public class ListPointsOfInterestAroundJob {
    private Observable<List<PointOfInterestBLLDTO>> observable;

    private boolean                  isAskingForMore;
    private Location                 currentLocation;
    private SearchRangeSettingBLLDTO searchRangeSetting;
    private PlaceType                placeType;

    ListPointsOfInterestAroundJob(
        GetCurrentLocationJob getCurrentLocationJob,
        ListPointsOfInterestJob listPointsOfInterestJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob
    ) {
        observable = Observable
            .create(new Observable.OnSubscribe<List<PointOfInterestBLLDTO>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterestBLLDTO>> subscriber) {
                    Subscriber<List<PointOfInterestBLLDTO>> listSubscriber;
                    Subscriber<SearchRangeSettingBLLDTO> settingSubscriber;

                    listSubscriber = new Subscriber<List<PointOfInterestBLLDTO>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<PointOfInterestBLLDTO> pointOfInterestBLLDTOs) {
                            subscriber.onNext(pointOfInterestBLLDTOs);
                        }
                    };

                    settingSubscriber = new Subscriber<SearchRangeSettingBLLDTO>() {
                        @Override
                        public void onCompleted() {
                            listPointsOfInterestJob
                                .get(isAskingForMore, currentLocation, searchRangeSetting, placeType)
                                .observeOn(Schedulers.newThread())
                                .subscribe(listSubscriber);
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(SearchRangeSettingBLLDTO searchRangeSettingBLLDTO) {
                            searchRangeSetting = searchRangeSettingBLLDTO;
                        }
                    };

                    getCurrentLocationJob
                        .get()
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Observer<Location>() {
                            @Override
                            public void onCompleted() {
                                getSearchRangeSettingJob
                                    .get()
                                    .observeOn(Schedulers.newThread())
                                    .subscribe(settingSubscriber);
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Location location) {
                                currentLocation = location;
                            }
                        });
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<List<PointOfInterestBLLDTO>> get(
        boolean isAskingForMore,
        PlaceType placeType) {
        this.isAskingForMore = isAskingForMore;
        this.placeType = placeType;

        return observable;
    }
}
