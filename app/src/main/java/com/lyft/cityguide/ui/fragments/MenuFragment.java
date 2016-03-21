package com.lyft.cityguide.ui.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.events.ShowSettings;
import com.lyft.cityguide.ui.events.ToggleMenuEvent;
import com.nineoldandroids.animation.Animator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * @class MenuFragment
 * @brief Handles aside menu
 */
public class MenuFragment extends BaseFragment {
    private boolean _isVisible;

    private Point _startPoint;

    @Bind(R.id.fragment_menu)
    View _wrapper;

    void show() {
        _isVisible = true;
        _wrapper.setVisibility(View.VISIBLE);
        YoYo
            .with(Techniques.SlideInLeft)
            .duration(300)
            .playOn(_wrapper);
    }

    void hide() {
        _isVisible = false;
        YoYo
            .with(Techniques.SlideOutLeft)
            .duration(300)
            .withListener(
                new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        _wrapper.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }
            )
            .playOn(_wrapper);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment;

        fragment = inflater.inflate(R.layout.partial_menu, container, false);
        ButterKnife.bind(this, fragment);

        _isVisible = false;

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        BaseFragment.getMenuBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        BaseFragment.getMenuBus().unregister(this);
    }

    public void onEventMainThread(ToggleMenuEvent e) {
        if (_isVisible) {
            hide();
        } else {
            show();
        }
    }

    @OnTouch(R.id.fragment_menu)
    public boolean onTouch(View view, MotionEvent e) {
        // Hide if slightly touched
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            _startPoint = new Point(Math.round(e.getX()), Math.round(e.getY()));
        } else if (e.getAction() == MotionEvent.ACTION_MOVE && _startPoint != null) {
            int distance = Math.abs(_startPoint.x - Math.round(e.getX()));

            if (distance > 100) {
                _startPoint = null; // Prevent any extra call to hide
                hide();
            }
        }

        return true;
    }

    @OnClick(R.id.fragment_menu_settings)
    public void onSettingsClick(View v) {
        hide();
        getMenuBus().post(new ShowSettings());
    }
}
