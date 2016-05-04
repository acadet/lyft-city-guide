package com.lyft.cityguide.bll;

import com.lyft.cityguide.domain.SearchRangeSetting;

import rx.Observable;

/**
 * ISearchSettingBLL
 * <p>
 */
public interface ISearchSettingBLL {
    Observable<Void> update(SearchRangeSetting newValue);

    Observable<SearchRangeSetting> get();
}
