package com.lyft.cityguide.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.utils.MetricHelper;

/**
 * StarBar
 * <p>
 * Custom star bar component (similar to a rating bar)
 */
public class StarBar extends LinearLayout {
    private LinearLayout wrapper;

    private int currentRating;
    private int total;

    public StarBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        currentRating = 0;
        total = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.component_star_bar, this, true);

        wrapper = (LinearLayout) findViewById(R.id.component_star_bar_wrapper);

        // Collect custom attributes
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.Slider);
        setRating(styledAttributes.getInteger(R.styleable.StarBar_rating, 0));
        setNumbers(styledAttributes.getInteger(R.styleable.StarBar_numbers, 5));
    }

    public StarBar(Context context) {
        this(context, null);
    }

    public void setNumbers(int value) {
        LinearLayout.LayoutParams params =
            new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );

        wrapper.removeAllViewsInLayout();
        for (int i = 0; i < value; i++) {
            ImageView img = new ImageView(getContext());

            if (i < currentRating) {
                img.setImageResource(R.drawable.star_pink);
            } else {
                img.setImageResource(R.drawable.star_grey);
            }

            img.setLayoutParams(params);
            img.setAdjustViewBounds(true);
            img.setMaxWidth(Math.round(MetricHelper.toPixels(getContext(), 20)));
            img.setMaxHeight(Math.round(MetricHelper.toPixels(getContext(), 20)));

            wrapper.addView(img);
        }
        wrapper.invalidate();
        total = value;
    }

    public void setRating(int value) {
        currentRating = value;
        setNumbers(total);
    }
}
