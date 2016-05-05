package com.lyft.cityguide.bll;

import android.location.Location;

import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.domain.SearchRangeSetting;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * ListPointsOfInterestAroundJob
 * <p>
 */
class ListPointsOfInterestAroundJob {

    private static class Holder {
        final Object instructionLock = new Object();
        boolean hasOtherThreadCompleted = false;
        SearchRangeSetting searchRangeSetting;
        Location           currentLocation;
    }

    private final GetCurrentLocationJob    getCurrentLocationJob;
    private final ListPointsOfInterestJob  listPointsOfInterestJob;
    private final GetSearchRangeSettingJob getSearchRangeSettingJob;

    ListPointsOfInterestAroundJob(
        GetCurrentLocationJob getCurrentLocationJob,
        ListPointsOfInterestJob listPointsOfInterestJob,
        GetSearchRangeSettingJob getSearchRangeSettingJob
    ) {
        this.getCurrentLocationJob = getCurrentLocationJob;
        this.listPointsOfInterestJob = listPointsOfInterestJob;
        this.getSearchRangeSettingJob = getSearchRangeSettingJob;
    }

    private Observable<List<PointOfInterest>> create(boolean isAskingForMore, PointOfInterest.Kind placeKind) {
        return Observable
            .create(new Observable.OnSubscribe<List<PointOfInterest>>() {
                @Override
                public void call(Subscriber<? super List<PointOfInterest>> subscriber) {
                    final Holder threadHolder = new Holder();
                    final Subscriber<List<PointOfInterest>> listPOIsSubscriber = new Subscriber<List<PointOfInterest>>() {
                        @Override
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override
                        public void onNext(List<PointOfInterest> pointOfInterestBLLDTOs) {
                            subscriber.onNext(pointOfInterestBLLDTOs);
                        }
                    };
                    final Action0 completeJob = () -> {
                        listPointsOfInterestJob
                            .list(threadHolder.currentLocation, threadHolder.searchRangeSetting, placeKind)
                            .observeOn(Schedulers.immediate())
                            .subscribe(listPOIsSubscriber);
                    };

                    if (isAskingForMore) {
                        listPointsOfInterestJob
                            .more()
                            .observeOn(Schedulers.immediate())
                            .subscribe(listPOIsSubscriber);
                    } else {
                        getCurrentLocationJob
                            .create()
                            .observeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<Location>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(Location location) {
                                    threadHolder.currentLocation = location;

                                    if (!threadHolder.hasOtherThreadCompleted) {
                                        synchronized (threadHolder.instructionLock) {
                                            if (!threadHolder.hasOtherThreadCompleted) {
                                                threadHolder.hasOtherThreadCompleted = true;
                                                return;
                                            }
                                        }
                                    }

                                    completeJob.call();
                                }
                            });

                        getSearchRangeSettingJob
                            .create()
                            .observeOn(Schedulers.newThread())
                            .subscribe(new Subscriber<SearchRangeSetting>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onNext(SearchRangeSetting value) {
                                    threadHolder.searchRangeSetting = value;

                                    if (!threadHolder.hasOtherThreadCompleted) {
                                        synchronized (threadHolder.instructionLock) {
                                            if (!threadHolder.hasOtherThreadCompleted) {
                                                threadHolder.hasOtherThreadCompleted = true;
                                                return;
                                            }
                                        }
                                    }

                                    completeJob.call();
                                }
                            });
                    }
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    Observable<List<PointOfInterest>> more() {
        return create(true, null);
    }

    Observable<List<PointOfInterest>> listAround(PointOfInterest.Kind placeKind) {
        return create(false, placeKind);
    }
}
