package com.lyft.cityguide.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.utils.MetricHelper;

/**
 * @class Slider
 * @brief Custom slider component
 */
public class Slider extends LinearLayout {
    /**
     * @class SliderListener
     * @brief Custom listener for this component. Triggered when a new value has been set
     */
    public interface SliderListener {
        void onSlide(int index, CharSequence label);
    }

    private LinearLayout _labelWrapper;

    // For internal purpose only
    private int            _currentIndex;
    private SliderListener _listener;

    // Custom attributes
    private CharSequence[] _labels;
    private int            _selectedForeground;
    private int            _defaultForeground;
    private float          _fontSize;

    public Slider(Context context, AttributeSet attributes) {
        super(context, attributes);

        _currentIndex = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.component_slider, this, true);

        _labelWrapper = (LinearLayout) findViewById(R.id.component_slider_label_wrapper);
        _labelWrapper.setOnTouchListener(
            new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int width = _labelWrapper.getWidth();
                    int n = _labels.length;
                    int touchedIndex = Math.round(event.getX()) / (width / n);

                    if (touchedIndex != _currentIndex) {
                        _setFieldAsDefault((TextView) _labelWrapper.getChildAt(_currentIndex));
                        _setFieldAsCurrent((TextView) _labelWrapper.getChildAt(touchedIndex));

                        _currentIndex = touchedIndex;
                        if (_listener != null) {
                            _listener.onSlide(touchedIndex, _labels[touchedIndex]);
                        }
                    }

                    return true;
                }
            }
        );

        // Collect custom attributes
        TypedArray styledAttributes = context.obtainStyledAttributes(attributes, R.styleable.Slider);
        _selectedForeground = styledAttributes.getColor(
            R.styleable.Slider_selectedForeground, getResources().getColor(android.R.color.black)
        );
        _defaultForeground = styledAttributes.getColor(
            R.styleable.Slider_defaultForeground, getResources().getColor(android.R.color.black)
        );
        _fontSize = styledAttributes.getDimension(R.styleable.Slider_fontSize, 12f);

        // Set content
        setLabels(styledAttributes.getTextArray(R.styleable.Slider_labels));
    }

    public Slider(Context context) {
        this(context, null);
    }

    private void _setFieldAsCurrent(TextView field) {
        field.setTextColor(_selectedForeground);
        field.setBackgroundResource(R.drawable.slider_thumb);
    }

    private void _setFieldAsDefault(TextView field) {
        field.setTextColor(_defaultForeground);
        field.setBackgroundResource(0);
    }

    /**
     * Builds the internal content of the slider
     *
     * @param labels
     */
    public void setLabels(CharSequence[] labels) {
        int i = 0;
        LinearLayout.LayoutParams params;
        int padding = Math.round(MetricHelper.toPixels(getContext(), 10));

        params = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        );

        _labelWrapper.removeAllViewsInLayout();
        for (CharSequence c : labels) {
            TextView t = new TextView(getContext());

            t.setLayoutParams(params);
            t.setTextSize(_fontSize);
            t.setGravity(Gravity.CENTER);
            t.setPadding(padding, padding, padding, padding);

            if (i == _currentIndex) {
                _setFieldAsCurrent(t);
            } else {
                _setFieldAsDefault(t);
            }

            t.setText(c);
            _labelWrapper.addView(t);
            i++;
        }

        _labels = labels;
        invalidate();
    }

    public void setSelectedForeground(int color) {
        _selectedForeground = color;
        ((TextView) _labelWrapper.getChildAt(_currentIndex)).setTextColor(_selectedForeground);
    }

    public void setDefaultForeground(int color) {
        _defaultForeground = color;
        for (int i = 0, s = _labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) _labelWrapper.getChildAt(i);

            if (i != _currentIndex) {
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

    public void setOnSlideListener(SliderListener listener) {
        _listener = listener;
    }
}
