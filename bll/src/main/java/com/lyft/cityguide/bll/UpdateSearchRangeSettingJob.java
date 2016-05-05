package com.lyft.cityguide.bll;

import com.lyft.cityguide.dao.ISettingsDAO;
import com.lyft.cityguide.domain.SearchRangeSetting;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * UpdateSearchRangeSettingJob
 * <p>
 */
class UpdateSearchRangeSettingJob {
    private final ISettingsDAO settingsDAO;

    UpdateSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        this.settingsDAO = settingsDAO;
    }

    Observable<Void> create(SearchRangeSetting newValue) {
        return Observable
            .create(new Observable.OnSubscribe<Void>() {
                @Override
                public void call(Subscriber<? super Void> subscriber) {
                    settingsDAO.saveSearchRange(newValue);
                    subscriber.onCompleted();
                }
            })
            .subscribeOn(Schedulers.newThread());
    }
}
