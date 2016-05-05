package com.lyft.cityguide.dao;

import timber.log.Timber;

/**
 * SearchRangeSettingDTO
 * <p>
 */
enum SearchRangeSettingDTO {
    ONE_MILE(0),
    TWO_MILE(1),
    FIVE_MILE(2);

    private int value;

    SearchRangeSettingDTO(int value) {
        this.value = value;
    }

    public int toInt() {
        return value;
    }

    public static SearchRangeSettingDTO fromInt(int value) {
        switch (value) {
            case 0:
                return ONE_MILE;
            case 1:
                return TWO_MILE;
            case 2:
                return FIVE_MILE;
            default:
                Timber.e("Unexpected value for SearchRangeSettingDTO");
                return null;
        }
    }
}
