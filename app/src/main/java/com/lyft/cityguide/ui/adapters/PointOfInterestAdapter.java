package com.lyft.cityguide.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lyft.cityguide.R;
import com.lyft.cityguide.domain.PointOfInterest;
import com.lyft.cityguide.ui.components.StarBar;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class PointOfInterestAdapter
 * @brief
 */
public class PointOfInterestAdapter extends BaseAdapter<PointOfInterest> {

    static class Holder {
        @Bind(R.id.adapter_result_icon)
        ImageView icon;

        @Bind(R.id.adapter_result_name)
        TextView name;

        @Bind(R.id.adapter_result_distance)
        TextView distance;

        @Bind(R.id.adapter_result_rating)
        StarBar rating;

        Holder(View source) {
            ButterKnife.bind(this, source);
        }
    }

    private PointOfInterest.Kind kind;

    public PointOfInterestAdapter(Context context, List<PointOfInterest> items, PointOfInterest.Kind kind) {
        super(context, items);
        this.kind = kind;
    }

    private void setUserFriendlyDate(TextView field, double value) {
        String text;
        DecimalFormat format = new DecimalFormat("#.##");

        text = format.format(value);
        text += " mi";

        field.setText(text);
    }

    private int getIcon() {
        switch (kind) {
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
        View view;
        Holder holder;
        PointOfInterest currentPOI;

        view = recycle(R.layout.adapter_point_of_interest, convertView, parent);

        if (convertView == null) {
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        currentPOI = itemAt(position);

        holder.icon.setImageResource(getIcon());
        holder.name.setText(currentPOI.getName());

        if (currentPOI.getDistance() != null) {
            holder.distance.setVisibility(View.VISIBLE);
            setUserFriendlyDate(holder.distance, currentPOI.getDistance().toMiles());
        } else {
            holder.distance.setVisibility(View.INVISIBLE);
        }

        holder.rating.setRating(Math.round(currentPOI.getRating()));

        YoYo.with(Techniques.FadeIn)
            .duration(500)
            .playOn(view);

        return view;
    }
}
