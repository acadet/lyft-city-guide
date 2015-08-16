package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.components.Slider;
import com.lyft.cityguide.ui.events.ShowBarsEvent;
import com.lyft.cityguide.ui.events.ShowBistrosEvent;
import com.lyft.cityguide.ui.events.ShowCafesEvent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class HeaderFragment
 * @brief
 */
public class HeaderFragment extends BaseFragment {
    @Bind(R.id.fragment_header_slider)
    Slider _slider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_header, container, false);
        ButterKnife.bind(this, fragment);

        _slider.setOnSlideListener(
            (index, label) -> {
                switch (index) {
                    case 0:
                        BaseFragment.getResultListBus().post(new ShowBarsEvent());
                        break;
                    case 1:
                        BaseFragment.getResultListBus().post(new ShowBistrosEvent());
                        break;
                    case 2:
                        BaseFragment.getResultListBus().post(new ShowCafesEvent());
                        break;
                }
            }
        );

        return fragment;
    }
}
