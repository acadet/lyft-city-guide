package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyft.cityguide.R;

import butterknife.ButterKnife;

/**
 * @class MainHeaderFragment
 * @brief
 */
public class MainHeaderFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_main_header, container, false);
        ButterKnife.bind(this, fragment);

        return fragment;
    }
}
