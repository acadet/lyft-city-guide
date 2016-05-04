package com.lyft.cityguide.ui.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.screens.LandingScreen;
import com.lyft.scoop.Scoop;

/**
 * MainActivity
 */
public class MainActivity extends BaseActivity {

    private Scoop rootScoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rootScoop = new Scoop.Builder("root").build();
        rootScoop.inflate(R.layout.root, (ViewGroup) findViewById(R.id.main_layout), true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!appRouter.hasActiveScreen()) {
            appRouter.goTo(new LandingScreen());
        }
    }

    @Override
    public void onBackPressed() {
        if (!appRouter.goBack()) {
            finish();
        }
    }
}
