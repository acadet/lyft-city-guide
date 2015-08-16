package com.lyft.cityguide.models.bll.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.api.interfaces.IGooglePlaceAPIOutlet;
import com.lyft.cityguide.models.bll.serializers.PlaceSearchResultSerializer;
import com.lyft.cityguide.models.bll.utils.PlaceSearchResult;
import com.lyft.cityguide.utils.actions.Action;
import com.lyft.cityguide.utils.actions.Action0;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @class GooglePlaceAPIOutlet
 * @brief
 */
class GooglePlaceAPIOutlet implements IGooglePlaceAPIOutlet {
    private Context _context;

    GooglePlaceAPIOutlet(Context context) {
        _context = context;
    }

    private RestAdapter.Builder _build() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(PlaceSearchResult.class, new PlaceSearchResultSerializer())
            .create();

        return new RestAdapter.Builder()
            .setEndpoint(_context.getString(R.string.api_endpoint))
            .setConverter(new GsonConverter(gson));
    }


    @Override
    public void connect(Action<GooglePlaceAPI> success, Action0 failure) {
        GooglePlaceAPI api = null;

        try {
            api = _build().build().create(GooglePlaceAPI.class);
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
