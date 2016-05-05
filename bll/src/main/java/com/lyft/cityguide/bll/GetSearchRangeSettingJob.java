package com.lyft.cityguide.bll;

import com.lyft.cityguide.dao.ISettingsDAO;
import com.lyft.cityguide.domain.SearchRangeSetting;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * GetSearchRangeSettingJob
 * <p>
 */
class GetSearchRangeSettingJob {

    private final ISettingsDAO settingsDAO;

    GetSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        this.settingsDAO = settingsDAO;
    }

    Observable<SearchRangeSetting> create() {
        return Observable
            .create(new Observable.OnSubscribe<SearchRangeSetting>() {
                @Override
                public void call(Subscriber<? super SearchRangeSetting> subscriber) {
                    subscriber.onNext(settingsDAO.getSearchRange());
                    subscriber.onCompleted();
                }
            })
            .subscribeOn(Schedulers.newThread());
    }
}
