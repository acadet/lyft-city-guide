package com.lyft.cityguide.ui.controllers;

import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lyft.cityguide.R;
import com.lyft.cityguide.domain.SearchRangeSetting;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * SettingsController
 * <p>
 */
public class SettingsController extends BaseController {
    private Subscription getSubscription;
    private Subscription updateSubscription;

    @BindColor(R.color.black)
    int blackColor;

    @BindColor(R.color.gray)
    int grayColor;

    @Bind(R.id.partial_settings_body_range_seek_bar)
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

    private void toggleLabelsAndSetProgress(SearchRangeSetting setting) {
        int value;

        switch (setting) {
            case ONE_MILE:
                value = 0;
                break;
            case TWO_MILE:
                value = 1;
                break;
            case FIVE_MILE:
                value = 2;
                break;
            default:
                value = -1;
                Timber.e("Unexpected value for search range setting");
        }

        seekBar.setProgress(value);

        toggleLabels(value);
    }

    private SearchRangeSetting settingFromProgress(int value) {
        SearchRangeSetting outcome;

        switch (value) {
            case 0:
                outcome = SearchRangeSetting.ONE_MILE;
                break;
            case 1:
                outcome = SearchRangeSetting.TWO_MILE;
                break;
            case 2:
                outcome = SearchRangeSetting.FIVE_MILE;
                break;
            default:
                outcome = null;
                Timber.e("Unexpected progress");
        }

        return outcome;
    }

    @Override
    protected int layoutId() {
        return R.layout.settings_layout;
    }

    @Override
    public void onAttach() {
        super.onAttach();

        getSubscription = searchSettingBLL
            .get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new BaseSubscriber<SearchRangeSetting>() {
                @Override
                public void onCompleted() {
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            toggleLabels(progress);

                            if (updateSubscription != null) {
                                updateSubscription.unsubscribe();
                            }

                            updateSubscription = searchSettingBLL
                                .update(settingFromProgress(progress))
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseSubscriber<Void>() {
                                    @Override
                                    public void onCompleted() {
                                        confirm(context.getString(R.string.settings_save_confirmation));
                                    }

                                    @Override
                                    public void onNext(Void aVoid) {

                                    }
                                });
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
                public void onNext(SearchRangeSetting setting) {
                    toggleLabelsAndSetProgress(setting);
                }
            });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (getSubscription != null) {
            getSubscription.unsubscribe();
        }

        if (updateSubscription != null) {
            updateSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.partial_settings_header_back)
    public void onBackClick() {
        appRouter.goBack();
    }
}
