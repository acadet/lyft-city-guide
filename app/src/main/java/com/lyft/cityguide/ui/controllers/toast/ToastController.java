package com.lyft.cityguide.ui.controllers.toast;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.lyft.cityguide.CityGuideApplication;
import com.lyft.cityguide.R;
import com.lyft.cityguide.ui.routers.IRouter;
import com.lyft.cityguide.ui.screens.toast.AlertScreen;
import com.lyft.cityguide.ui.screens.toast.ConfirmScreen;
import com.lyft.cityguide.ui.screens.toast.InformScreen;
import com.lyft.scoop.Screen;
import com.lyft.scoop.ViewController;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * ToastController
 * <p>
 */
public class ToastController extends ViewController {
    private Handler autoHidingHandler;

    @Inject
    @Named("toast")
    IRouter router;

    @Bind(R.id.toast)
    TextView textView;

    private void setContent(String message, int colorId) {
        textView.setText(message);
        textView.setBackgroundResource(colorId);
    }

    @Override
    protected int layoutId() {
        return R.layout.toast;
    }

    @Override
    public void onAttach() {
        super.onAttach();

        CityGuideApplication.getApplicationComponent().inject(this);
        Screen screen = Screen.fromController(this);

        if (screen instanceof AlertScreen) {
            setContent(((AlertScreen) screen).message, R.color.alert);
        } else if (screen instanceof ConfirmScreen) {
            setContent(((ConfirmScreen) screen).message, R.color.confirm);
        } else if (screen instanceof InformScreen) {
            setContent(((InformScreen) screen).message, R.color.inform);
        } else {
            Timber.e("Unexpected screen for ToastController");
        }

        autoHidingHandler = new Handler(Looper.getMainLooper());
        autoHidingHandler.postDelayed(() -> {
            router.goBack();
        }, 5 * 1000);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (autoHidingHandler != null) {
            autoHidingHandler.removeCallbacksAndMessages(null);
        }
    }

    @OnClick(R.id.toast)
    public void onToastClicked() {
        router.goBack();
    }
}
