package com.lyft.cityguide.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lyft.cityguide.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @class ResultListFragment
 * @brief
 */
public class ResultListFragment extends BaseFragment {
    @Bind(R.id.fragment_result_list_no_content)
    TextView _noContent;

    @Bind(R.id.fragment_result_list)
    ListView _list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.fragment_result_list, container);
        ButterKnife.bind(this, fragment);

        return fragment;
    }
}
