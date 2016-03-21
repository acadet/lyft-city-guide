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
 * Slider
 * <p>
 * Custom slider component
 */
public class Slider extends LinearLayout {
    /**
     * Observer
     * <p>
     * Custom observer for this component. Triggered when a new value has been set
     */
    public interface Observer {
        void onSlide(int index, CharSequence label);
    }

    private LinearLayout labelWrapper;

    // For internal purpose only
    private int      currentIndex;
    private Observer observer;

    // Custom attributes
    private CharSequence[] labels;
    private int            selectedForeground;
    private int            defaultForeground;
    private float          fontsize;

    public Slider(Context context, AttributeSet attributes) {
        super(context, attributes);

        currentIndex = 0;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.component_slider, this, true);

        labelWrapper = (LinearLayout) findViewById(R.id.component_slider_label_wrapper);
        labelWrapper.setOnTouchListener(
            new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int width = labelWrapper.getWidth();
                    int n = labels.length;
                    int touchedIndex = Math.round(event.getX()) / (width / n);

                    if (touchedIndex != currentIndex) {
                        setFieldAsDefault((TextView) labelWrapper.getChildAt(currentIndex));
                        setFieldAsCurrent((TextView) labelWrapper.getChildAt(touchedIndex));

                        currentIndex = touchedIndex;
                    }

                    if (observer != null && event.getAction() == MotionEvent.ACTION_UP) {
                        observer.onSlide(currentIndex, labels[currentIndex]);
                    }

                    return true;
                }
            }
        );

        // Collect custom attributes
        TypedArray styledAttributes = context.obtainStyledAttributes(attributes, R.styleable.Slider);
        selectedForeground = styledAttributes.getColor(
            R.styleable.Slider_selectedForeground, getResources().getColor(android.R.color.black)
        );
        defaultForeground = styledAttributes.getColor(
            R.styleable.Slider_defaultForeground, getResources().getColor(android.R.color.black)
        );
        fontsize = styledAttributes.getDimension(R.styleable.Slider_fontSize, 12f);

        // Set content
        setLabels(styledAttributes.getTextArray(R.styleable.Slider_labels));
    }

    public Slider(Context context) {
        this(context, null);
    }

    private void setFieldAsCurrent(TextView field) {
        field.setTextColor(selectedForeground);
        field.setBackgroundResource(R.drawable.slider_thumb);
    }

    private void setFieldAsDefault(TextView field) {
        field.setTextColor(defaultForeground);
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

        labelWrapper.removeAllViewsInLayout();
        for (CharSequence c : labels) {
            TextView t = new TextView(getContext());

            t.setLayoutParams(params);
            t.setTextSize(fontsize);
            t.setGravity(Gravity.CENTER);
            t.setPadding(padding, padding, padding, padding);

            if (i == currentIndex) {
                setFieldAsCurrent(t);
            } else {
                setFieldAsDefault(t);
            }

            t.setText(c);
            labelWrapper.addView(t);
            i++;
        }

        this.labels = labels;
        invalidate();
    }

    public void setSelectedForeground(int color) {
        selectedForeground = color;
        ((TextView) labelWrapper.getChildAt(currentIndex)).setTextColor(selectedForeground);
    }

    public void setDefaultForeground(int color) {
        defaultForeground = color;
        for (int i = 0, s = labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) labelWrapper.getChildAt(i);

            if (i != currentIndex) {
                t.setTextColor(defaultForeground);
            }
        }
    }

    public void setFontSize(float size) {
        fontsize = size;
        for (int i = 0, s = labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) labelWrapper.getChildAt(i);

            t.setTextSize(fontsize);
        }
    }

    public void observe(Observer observer) {
        this.observer = observer;
    }
}
