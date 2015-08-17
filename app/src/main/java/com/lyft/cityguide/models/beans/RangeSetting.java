package com.lyft.cityguide.models.beans;

public enum RangeSetting {
    ONE_MILE(0),
    TWO_MILE(1),
    FIVE_MILE(2);

    private int _value;

    RangeSetting(int value) {
        _value = value;
    }

    public int toInt() {
        return _value;
    }

    public static RangeSetting fromInt(int value) {
        switch (value) {
            case 0:
                return ONE_MILE;
            case 1:
                return TWO_MILE;
            default:
                return FIVE_MILE;
        }
    }
}
