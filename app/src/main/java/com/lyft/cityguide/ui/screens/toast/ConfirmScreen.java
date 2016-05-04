package com.lyft.cityguide.ui.screens.toast;

import com.lyft.cityguide.ui.controllers.toast.ToastController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.Screen;

/**
 * ConfirmScreen
 * <p>
 */
@Controller(ToastController.class)
public class ConfirmScreen extends Screen {
    public final String message;

    public ConfirmScreen(String message) {
        this.message = message;
    }
}
