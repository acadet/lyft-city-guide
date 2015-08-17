package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.beans.RangeSetting;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class SettingsFragment
 * @brief
 */
public class SettingsFragment extends BaseFragment {
    @Bind(R.id.fragment_settings_range_seek_bar)
    SeekBar _rangeBar;

    @Bind(R.id.fragment_settings_range_1)
    TextView _oneMileRange;

    @Bind(R.id.fragment_settings_range_2)
    TextView _twoMileRange;

    @Bind(R.id.fragment_settings_range_5)
    TextView _fiveMileRange;

    private TextView[] _rangeLabels;

    private void _setLabels(int progress) {
        for (int i = 0; i < 3; i++) {
            int color;

            if (i == progress) {
                color = getResources().getColor(R.color.black);
            } else {
                color = getResources().getColor(R.color.gray);
            }

            _rangeLabels[i].setTextColor(color);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, fragment);

        _rangeLabels = new TextView[] { _oneMileRange, _twoMileRange, _fiveMileRange };
        int progress = getSettingsBLL().get().toInt();
        _rangeBar.setProgress(progress);
        _setLabels(progress);
        _rangeBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    for (int i = 0; i < 3; i++) {
                        int color;

                        if (i == progress) {
                            color = getResources().getColor(R.color.black);
                        } else {
                            color = getResources().getColor(R.color.gray);
                        }

                        _rangeLabels[i].setTextColor(color);
                    }

                    getSettingsBLL().save(RangeSetting.fromInt(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }
        );

        return fragment;
    }
}