package com.lyft.cityguide.models.bll.interfaces;

import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;

import rx.Observable;

/**
 * IDataWritingBLL
 * <p>
 */
public interface IDataWritingBLL {
    Observable<Void> updateSearchRangeSetting(SearchRangeSettingBLLDTO setting);
}
