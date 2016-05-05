package com.lyft.cityguide.dao;

import com.lyft.cityguide.domain.SearchRangeSetting;

import timber.log.Timber;

/**
 * SearchRangeSettingMapper
 * <p>
 */
class SearchRangeSettingMapper implements ISearchRangeSettingMapper {
    @Override
    public SearchRangeSetting map(SearchRangeSettingDTO source) {
        SearchRangeSetting outcome;

        switch (source) {
            case ONE_MILE:
                outcome = SearchRangeSetting.ONE_MILE;
                break;
            case TWO_MILE:
                outcome = SearchRangeSetting.TWO_MILE;
                break;
            case FIVE_MILE:
                outcome = SearchRangeSetting.FIVE_MILE;
                break;
            default:
                outcome = null;
                Timber.e("Unexpected value for SearchRangeSettingDTO");
                break;
        }

        return outcome;
    }

    @Override
    public SearchRangeSettingDTO map(SearchRangeSetting source) {
        SearchRangeSettingDTO outcome;

        switch (source) {
            case ONE_MILE:
                outcome = SearchRangeSettingDTO.ONE_MILE;
                break;
            case TWO_MILE:
                outcome = SearchRangeSettingDTO.TWO_MILE;
                break;
            case FIVE_MILE:
                outcome = SearchRangeSettingDTO.FIVE_MILE;
                break;
            default:
                outcome = null;
                Timber.e("Unexpected value for SearchRangeSetting");
                break;
        }

        return outcome;
    }
}
