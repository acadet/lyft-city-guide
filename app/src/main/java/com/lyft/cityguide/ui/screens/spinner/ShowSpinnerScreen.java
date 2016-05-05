package com.lyft.cityguide.ui.screens.spinner;

import com.lyft.cityguide.ui.controllers.spinner.SpinnerController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.Screen;

/**
 * ShowSpinnerScreen
 * <p>
 */
@Controller(SpinnerController.class)
public class ShowSpinnerScreen extends Screen {
    public final boolean isInstant;

    public ShowSpinnerScreen() {
        isInstant = false;
    }

    public ShowSpinnerScreen(boolean isInstant) {
        this.isInstant = isInstant;
    }
}
