package com.lyft.cityguide.ui.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.components.MainUIContainer;
import com.lyft.cityguide.ui.screens.LandingScreen;
import com.lyft.scoop.Scoop;

import butterknife.Bind;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.main_layout)
    ViewGroup mainLayout;

    @Bind(R.id.main_ui_container)
    MainUIContainer container;

    private Scoop rootScoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rootScoop = new Scoop.Builder("root").build();
        rootScoop.inflate(R.layout.root_layout, mainLayout, true);

        appRouter.goTo(new LandingScreen());
    }
}
