package com.lyft.cityguide.ui.controllers.menu;

import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.cityguide.ui.screens.SettingsScreen;
import com.lyft.scoop.ViewController;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * MenuController
 * <p>
 */
public class MenuController extends ViewController {

    private Point menuStartTouchPoint;

    @Inject
    @Named("app")
    IRouter appRouter;

    @Inject
    @Named("menu")
    IRouter menuRouter;

    @Override
    protected int layoutId() {
        return R.layout.menu;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        CityGuideApplication.getApplicationComponent().inject(this);
    }

    @OnTouch(R.id.menu)
    public boolean onMenuViewTouch(View view, MotionEvent e) {
        // Hide if slightly touched
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            menuStartTouchPoint = new Point(Math.round(e.getX()), Math.round(e.getY()));
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && menuStartTouchPoint != null) {
            int distance = menuStartTouchPoint.x - Math.round(e.getX());

            if (distance > 100) {
                menuStartTouchPoint = null; // Prevent any extra call to hide
                menuRouter.goBack();
            }
        }

        return true;
    }

    @OnClick(R.id.partial_menu_settings)
    public void onMenuSettingsClick() {
        appRouter.goTo(new SettingsScreen());
        menuRouter.goBack();
    }
}
