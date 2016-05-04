package com.lyft.cityguide.bll;

import android.content.Context;
import com.lyft.cityguide.bll.R;

/**
 * Configuration
 * <p>
 */
class Configuration {
    final int LOCATION_FETCHING_COOLDOWN_IN_SECONDS;

    Configuration(Context context) {
        LOCATION_FETCHING_COOLDOWN_IN_SECONDS = context.getResources().getInteger(R.integer.location_fetching_cooldown_in_seconds);
    }
}
