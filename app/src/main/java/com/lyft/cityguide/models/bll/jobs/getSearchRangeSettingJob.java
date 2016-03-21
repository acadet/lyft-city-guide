package com.lyft.cityguide.models.bll.jobs;

import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;
import com.lyft.cityguide.models.dao.ISettingsDAO;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * GetSearchRangeSettingJob
 * <p>
 */
public class GetSearchRangeSettingJob {
    private Observable<SearchRangeSettingBLLDTO> observable;

    GetSearchRangeSettingJob(ISettingsDAO settingsDAO) {
        observable = Observable
            .create(new Observable.OnSubscribe<SearchRangeSettingBLLDTO>() {
                @Override
                public void call(Subscriber<? super SearchRangeSettingBLLDTO> subscriber) {
                    subscriber.onNext(SearchRangeSettingBLLDTO.fromInt(settingsDAO.getSearchRange().toInt()));
                    subscriber.onCompleted();
                }
            })
            .subscribeOn(Schedulers.newThread());
    }

    public Observable<SearchRangeSettingBLLDTO> get() {
        return observable;
    }
}
