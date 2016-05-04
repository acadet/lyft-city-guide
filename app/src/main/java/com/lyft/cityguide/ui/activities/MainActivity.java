package com.lyft.cityguide.ui.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.containers.MainUIContainer;
import com.lyft.cityguide.ui.screens.LandingScreen;
import com.lyft.scoop.Scoop;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.main_ui_container)
    MainUIContainer container;

    private Scoop rootScoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rootScoop = new Scoop.Builder("root").build();
        rootScoop.inflate(R.layout.root_layout, (ViewGroup) findViewById(R.id.main_layout), true);
        ButterKnife.bind(this);

        appRouter.goTo(new LandingScreen());
    }

    @Override
    public void onBackPressed() {
        if (!appRouter.goBack()) {
            finish();
        }
    }
}
