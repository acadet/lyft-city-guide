package com.lyft.cityguide.ui.screens.toast;

import com.lyft.cityguide.ui.controllers.toast.ToastController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.Screen;

/**
 * AlertScreen
 * <p>
 */
@Controller(ToastController.class)
public class AlertScreen extends Screen {
    public final String message;

    public AlertScreen(String message) {
        this.message = message;
    }
}
