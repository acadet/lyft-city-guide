package com.lyft.cityguide.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.structs.PlaceType;
import com.lyft.cityguide.ui.components.StarBar;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @class PointOfInterestAdapter
 * @brief
 */
public class PointOfInterestAdapter extends BaseAdapter<PointOfInterestBLLDTO> {

    private PlaceType currentType;

    public PointOfInterestAdapter(Context context, List<PointOfInterestBLLDTO> items, PlaceType type) {
        super(context, items);
        currentType = type;
    }

    private void setUserFriendlyDate(TextView field, double value) {
        String text;
        DecimalFormat format = new DecimalFormat("#.##");

        text = format.format(value);
        text += " mi";

        field.setText(text);
    }

    private int getIcon() {
        switch (currentType) {
            case BAR:
                return R.drawable.ic_bar;
            case BISTRO:
                return R.drawable.ic_bistro;
            case CAFE:
                return R.drawable.ic_cafe;
            default:
                return R.drawable.ic_unknown;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View adapter;
        ImageView icon;
        TextView name, distance;
        StarBar rating;
        PointOfInterestBLLDTO currentPOI;

        adapter = recycle(R.layout.adapter_point_of_interest, convertView, parent);
        icon = ButterKnife.findById(adapter, R.id.adapter_result_icon);
        name = ButterKnife.findById(adapter, R.id.adapter_result_name);
        distance = ButterKnife.findById(adapter, R.id.adapter_result_distance);
        rating = ButterKnife.findById(adapter, R.id.adapter_result_rating);

        currentPOI = itemAt(position);

        icon.setImageResource(getIcon());
        name.setText(currentPOI.getName());
        if (currentPOI.getDistance() != null) {
            setUserFriendlyDate(distance, currentPOI.getDistance().toMiles());
        }
        rating.setRating(Math.round(currentPOI.getRating()));

        YoYo.with(Techniques.FadeIn)
            .duration(500)
            .playOn(adapter);

        return adapter;
    }

    public void setCurrentType(PlaceType type) {
        currentType = type;
    }
}
