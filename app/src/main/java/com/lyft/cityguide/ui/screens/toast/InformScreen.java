package com.lyft.cityguide.ui.screens.toast;

import com.lyft.cityguide.ui.controllers.toast.ToastController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.Screen;

/**
 * InformScreen
 * <p>
 */
@Controller(ToastController.class)
public class InformScreen extends Screen {
    public final String message;

    public InformScreen(String message) {
        this.message = message;
    }
}
