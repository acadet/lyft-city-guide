package com.lyft.cityguide.services.google.place;

import android.content.Context;
import com.lyft.cityguide.services.R;

/**
 * Configuration
 * <p>
 */
class Configuration {

    public final String API_ENDPOINT;

    public final String API_KEY;

    Configuration(Context context) {
        API_ENDPOINT = context.getString(R.string.google_place_api_endpoint);
        API_KEY = context.getString(R.string.google_api_key);
    }
}
