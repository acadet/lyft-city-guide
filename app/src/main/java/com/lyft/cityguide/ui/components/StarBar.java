package com.lyft.cityguide.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lyft.cityguide.R;

/**
 * @class StarBar
 * @brief
 */
public class StarBar extends LinearLayout {
    private LinearLayout _wrapper;

    private int _currentRating;
    private int _total;

    public StarBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        _currentRating = 0;
        _total = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.component_star_bar, this, true);

        _wrapper = (LinearLayout) findViewById(R.id.component_star_bar_wrapper);
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

        for (int i = 0; i < value; i++) {
            ImageView img = new ImageView(getContext());

            if (i < _currentRating) {
                img.setImageResource(R.drawable.star_pink);
            } else {
                img.setImageResource(R.drawable.star_grey);
            }

            _wrapper.addView(img);
        }
        _wrapper.invalidate();
        _total = value;
    }

    public void setRating(int value) {
        _currentRating = value;
        setNumbers(_total);
    }
}
