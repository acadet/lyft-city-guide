package com.lyft.cityguide.models.bll.jobs;

import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.dao.ISettingsDAO;
import com.lyft.cityguide.dao.SearchRangeSettingDAODTO;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * UpdateSearchRangeSettingJob
 * <p>
 */
public class UpdateSearchRangeSettingJob {
    private Observable<Void> observable;

    private SearchRangeSettingBLLDTO newValue;

    UpdateSearchRangeSettingJob(ISettingsDAO dao) {
        observable = Observable
            .create(new Observable.OnSubscribe<Void>() {
                @Override
                public void call(Subscriber<? super Void> subscriber) {
                    dao.saveSearchRange(SearchRangeSettingDAODTO.fromInt(newValue.toInt()));
                    subscriber.onCompleted();
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<Void> get(SearchRangeSettingBLLDTO newValue) {
        this.newValue = newValue;
        return observable;
    }
}
