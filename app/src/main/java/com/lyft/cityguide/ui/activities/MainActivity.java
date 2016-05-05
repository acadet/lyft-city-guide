package com.lyft.cityguide.ui.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.screens.LandingScreen;
import com.lyft.cityguide.ui.screens.menu.InitMenuScreen;
import com.lyft.cityguide.ui.screens.spinner.InitSpinnerScreen;
import com.lyft.cityguide.ui.screens.toast.InitToastScreen;
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

        toastRouter.resetTo(new InitToastScreen());
        spinnerRouter.resetTo(new InitSpinnerScreen());
        menuRouter.resetTo(new InitMenuScreen());
    }

    @Override
    public void onBackPressed() {
        if (menuRouter.hasActiveScreen()) {
            menuRouter.goBack();
            return;
        }

        if (!appRouter.goBack()) {
            finish();
        }
    }
}
