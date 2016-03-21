package com.lyft.cityguide.ui.controllers;

import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.models.bll.dto.SearchRangeSettingBLLDTO;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * SettingsController
 * <p>
 */
public class SettingsController extends BaseController {
    private Subscription getSearchRangeSettingSubscription;

    @BindColor(R.color.black)
    int blackColor;

    @BindColor(R.color.gray)
    int grayColor;

    @Bind(R.id.fragment_settings_range_seek_bar)
    SeekBar seekBar;

    @Bind(R.id.partial_settings_body_label_wrapper)
    ViewGroup labelWrapper;

    private void toggleLabels(int progress) {
        for (int i = 0, s = labelWrapper.getChildCount(); i < s; i++) {
            TextView t = (TextView) labelWrapper.getChildAt(i);
            int color = (i == progress) ? blackColor : grayColor;

            t.setTextColor(color);
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.settings_layout;
    }

    @Override
    public void onAttach() {
        super.onAttach();

        getSearchRangeSettingSubscription = dataReadingBLL
            .getSearchRangeSetting()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<SearchRangeSettingBLLDTO>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(SearchRangeSettingBLLDTO searchRangeSettingBLLDTO) {
                    toggleLabels(searchRangeSettingBLLDTO.toInt());
                }
            });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dataWritingBLL.updateSearchRangeSetting(SearchRangeSettingBLLDTO.fromInt(progress));
                toggleLabels(progress);
                confirm(context.getString(R.string.settings_save_confirmation));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getSearchRangeSettingSubscription != null) {
            getSearchRangeSettingSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.partial_settings_header_back)
    public void onBackClick() {
        appRouter.goBack();
    }
}
