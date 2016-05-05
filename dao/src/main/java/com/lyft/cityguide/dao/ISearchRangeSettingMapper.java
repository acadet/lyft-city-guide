package com.lyft.cityguide.dao;

import com.lyft.cityguide.domain.SearchRangeSetting;

/**
 * ISearchRangeSettingMapper
 * <p>
 */
interface ISearchRangeSettingMapper {
    SearchRangeSetting map(SearchRangeSettingDTO source);

    SearchRangeSettingDTO map(SearchRangeSetting source);
}
