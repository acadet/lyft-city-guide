package com.lyft.cityguide.ui.activities;

import android.app.Fragment;
import android.os.Bundle;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.fragments.HeaderFragment;
import com.lyft.cityguide.ui.fragments.MenuFragment;
import com.lyft.cityguide.ui.fragments.ResultListFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private void _setDefaultContent() {
        Map<Integer, Fragment> fragments = new HashMap<>();

        fragments.put(R.id.main_header, new HeaderFragment());
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
}
