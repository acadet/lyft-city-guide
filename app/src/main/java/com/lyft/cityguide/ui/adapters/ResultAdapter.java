package com.lyft.cityguide.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.structs.PointOfInterest;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @class ResultAdapter
 * @brief
 */
public class ResultAdapter extends BaseAdapter<PointOfInterest> {
    public ResultAdapter(List<PointOfInterest> items, Context context) {
        super(items, context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View adapter;
        ImageView icon;
        TextView name, distance;
        RatingBar rating;
        PointOfInterest currentPOI;

        adapter = recycle(convertView, R.layout.adapter_result, parent);
        icon = ButterKnife.findById(adapter, R.id.adapter_result_icon);
        name = ButterKnife.findById(adapter, R.id.adapter_result_name);
        distance = ButterKnife.findById(adapter, R.id.adapter_result_distance);
        rating = ButterKnife.findById(adapter, R.id.adapter_result_rating);

        currentPOI = itemAt(position);

        name.setText(currentPOI.getPlace().getName());
        distance.setText(new DecimalFormat("#.0").format(currentPOI.getDistance()));

        return adapter;
    }
}
