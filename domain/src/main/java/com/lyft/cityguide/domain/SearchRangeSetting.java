package com.lyft.cityguide.domain;

import timber.log.Timber;

/**
 * SearchRangeSetting
 * <p>
 */
public enum SearchRangeSetting {
    ONE_MILE(0),
    TWO_MILE(1),
    FIVE_MILE(2);

    private int value;

    SearchRangeSetting(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static SearchRangeSetting fromInt(int value) {
        switch (value) {
            case 0:
                return ONE_MILE;
            case 1:
                return TWO_MILE;
            case 2:
                return FIVE_MILE;
            default:
                Timber.e("Unexpected value for SearchRangeSettingDAODTO");
                return null;
        }
    }
}
