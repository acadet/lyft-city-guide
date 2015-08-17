package com.lyft.cityguide.ui.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.events.ShowResultList;
import com.lyft.cityguide.ui.events.ShowSettings;
import com.lyft.cityguide.ui.fragments.BaseFragment;
import com.lyft.cityguide.ui.fragments.MenuFragment;
import com.lyft.cityguide.ui.fragments.ResultListFragment;
import com.lyft.cityguide.ui.fragments.ResultListHeaderFragment;
import com.lyft.cityguide.ui.fragments.SettingsFragment;
import com.lyft.cityguide.ui.fragments.SettingsHeaderFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private void _setDefaultContent() {
        Map<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.main_header, new ResultListHeaderFragment());
        fragments.put(R.id.main_body, new ResultListFragment());
        fragments.put(R.id.main_menu, new MenuFragment());

        setFragments(fragments);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        _setDefaultContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        BaseFragment.getMenuBus().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        BaseFragment.getMenuBus().unregister(this);
    }

    public void onEventMainThread(ShowSettings e) {
        Map<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.main_header, new SettingsHeaderFragment());
        fragments.put(R.id.main_body, new SettingsFragment());

        setFragments(fragments);
    }

    public void onEventMainThread(ShowResultList e) {
        Map<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.main_header, new ResultListHeaderFragment());
        fragments.put(R.id.main_body, new ResultListFragment());

        setFragments(fragments);
    }
}
