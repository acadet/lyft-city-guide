package com.lyft.cityguide.ui.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @class MetricHelper
 * @brief
 */
public class MetricHelper {
    private static DisplayMetrics _getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static float toPixels(Context context, int dp) {
        return (dp * _getDisplayMetrics(context).densityDpi / 160f);
    }

    public static float toDps(Context context, int pixels) {
        return (pixels / _getDisplayMetrics(context).densityDpi * 160);
    }
}
