package com.lyft.cityguide.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.utils.MetricHelper;

/**
 * @class Slider
 * @brief
 */
public class Slider extends LinearLayout {
    private LinearLayout _labelWrapper;
    private View         _thumb;

    private int _current;

    private int   _selectedForeground;
    private int   _defaultForeground;
    private float _fontSize;

    public Slider(Context context, AttributeSet attributes) {
        super(context, attributes);

        _current = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.component_slider, this, true);

        _labelWrapper = (LinearLayout) findViewById(R.id.component_slider_label_wrapper);
        //        _thumb = findViewById(R.id.component_slider_thumb);

        TypedArray styledAttributes = context.obtainStyledAttributes(attributes, R.styleable.Slider);
        _selectedForeground = styledAttributes.getColor(
            R.styleable.Slider_selectedForeground, getResources().getColor(android.R.color.black)
        );
        _defaultForeground = styledAttributes.getColor(
            R.styleable.Slider_defaultForeground, getResources().getColor(android.R.color.black)
        );
        _fontSize = styledAttributes.getDimension(R.styleable.Slider_fontSize, 12f);

        setLabels(styledAttributes.getTextArray(R.styleable.Slider_labels));
    }

    public Slider(Context context) {
        this(context, null);
    }

    public void setLabels(CharSequence[] labels) {
        int i = 0;
        LinearLayout.LayoutParams params;
        int padding = Math.round(MetricHelper.toPixels(getContext(), 8));

        params = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        );

        _labelWrapper.removeAllViewsInLayout();
        for (CharSequence c : labels) {
            TextView t = new TextView(getContext());
            int color = (i == _current) ? _selectedForeground : _defaultForeground;

            t.setLayoutParams(params);
            t.setTextColor(color);
            t.setTextSize(_fontSize);
            t.setGravity(Gravity.CENTER);
            t.setPadding(padding, padding, padding, padding);

            if (i == _current) {
                t.setBackgroundResource(R.drawable.slider_thumb);
            } else {
                t.setBackgroundResource(0);
            }

            t.setText(c);
            _labelWrapper.addView(t);
            i++;
        }

        invalidate();
    }

    public void setSelectedForeground(int color) {
        _selectedForeground = color;
        ((TextView) _labelWrapper.getChildAt(_current)).setTextColor(_selectedForeground);
    }

    public void setDefaultForeground(int color) {
        _defaultForeground = color;
        for (int i = 0, s = _labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) _labelWrapper.getChildAt(i);

            if (i != _current) {
                t.setTextColor(_defaultForeground);
            }
        }
    }

    public void setFontSize(float size) {
        _fontSize = size;
        for (int i = 0, s = _labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) _labelWrapper.getChildAt(i);

            t.setTextSize(_fontSize);
        }
    }
}
