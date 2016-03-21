package com.lyft.cityguide.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lyft.cityguide.R;
import com.lyft.cityguide.models.structs.PlaceType;
import com.lyft.cityguide.models.bll.dto.PointOfInterestBLLDTO;
import com.lyft.cityguide.ui.components.StarBar;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @class ResultAdapter
 * @brief
 */
public class ResultAdapter extends BaseAdapter<PointOfInterestBLLDTO> {

    private PlaceType _currentType;

    public ResultAdapter(List<PointOfInterestBLLDTO> items, Context context, PlaceType type) {
        super(items, context);
        _currentType = type;
    }

    /**
     * Fills distance field with a user-friendly value
     *
     * @param field
     * @param value
     */
    private void _setDistance(TextView field, double value) {
        String text;

        if (value < 0.001) { // No value or too small
            field.setText("");
            return;
        }
        if (value < 1) {
            text = "0." + ((int) Math.floor(value * 100));
        } else {
            text = ((int) Math.floor(value)) + ".";
            text += ((int) (Math.floor(value * 100) - Math.floor(value) * 100));
        }

        text += " mi";

        field.setText(text);
    }

    private int _getIcon() {
        switch (_currentType) {
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

        adapter = recycle(convertView, R.layout.adapter_result, parent);
        icon = ButterKnife.findById(adapter, R.id.adapter_result_icon);
        name = ButterKnife.findById(adapter, R.id.adapter_result_name);
        distance = ButterKnife.findById(adapter, R.id.adapter_result_distance);
        rating = ButterKnife.findById(adapter, R.id.adapter_result_rating);

        currentPOI = itemAt(position);

        icon.setImageResource(_getIcon());
        name.setText(currentPOI.getPlace().getName());
        _setDistance(distance, currentPOI.getDistance().toMiles());
        rating.setRating(Math.round(currentPOI.getPlace().getRating()));

        YoYo.with(Techniques.FadeIn)
            .duration(500)
            .playOn(adapter);

        return adapter;
    }
}
