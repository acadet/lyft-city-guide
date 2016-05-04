package com.lyft.cityguide.services.google.distancematrix;

import android.content.Context;
import com.lyft.cityguide.services.R;

/**
 * Configuration
 * <p>
 */
class Configuration {
    final String API_ENDPOINT;

    final String API_KEY;

    Configuration(Context context) {
        API_ENDPOINT = context.getString(R.string.google_distance_matrix_api_endpoint);
        API_KEY = context.getString(R.string.google_api_key);
    }
}
