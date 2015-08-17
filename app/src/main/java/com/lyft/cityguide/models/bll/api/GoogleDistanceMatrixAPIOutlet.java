package com.lyft.cityguide.models.bll.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.api.interfaces.IGoogleDistanceMatrixAPIOutlet;
import com.lyft.cityguide.models.bll.serializers.DistanceResultSerializer;
import com.lyft.cityguide.models.bll.structs.DistanceResult;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @class GoogleDistanceMatrixAPIOutlet
 * @brief
 */
class GoogleDistanceMatrixAPIOutlet implements IGoogleDistanceMatrixAPIOutlet {
    private Context _context;

    GoogleDistanceMatrixAPIOutlet(Context context) {
        _context = context;
    }

    private RestAdapter.Builder _build() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(DistanceResult.class, new DistanceResultSerializer())
            .create();

        return new RestAdapter.Builder()
            .setEndpoint(_context.getString(R.string.google_distance_api_endpoint))
            .setConverter(new GsonConverter(gson));
    }

    @Override
    public void connect(Action<GoogleDistanceMatrixAPI> success, Action0 failure) {
        GoogleDistanceMatrixAPI api = null;

        try {
            api = _build().build().create(GoogleDistanceMatrixAPI.class);
        } catch (Exception e) {

        } finally {
            if (api == null) {
                failure.run();
            } else {
                success.run(api);
            }
        }
    }
}
