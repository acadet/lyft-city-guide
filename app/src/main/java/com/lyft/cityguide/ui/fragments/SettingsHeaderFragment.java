package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.events.ShowResultList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @class SettingsHeaderFragment
 * @brief
 */
public class SettingsHeaderFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_settings_header, container, false);
        ButterKnife.bind(this, fragment);

        return fragment;
    }

    @OnClick(R.id.fragment_settings_header_back)
    public void onBack(View v) {
        BaseFragment.getMenuBus().post(new ShowResultList());
    }
}
