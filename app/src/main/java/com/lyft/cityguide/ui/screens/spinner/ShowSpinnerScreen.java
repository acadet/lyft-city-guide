package com.lyft.cityguide.ui.screens.spinner;

import com.lyft.scoop.Screen;

/**
 * ShowSpinnerScreen
 * <p>
 */
public class ShowSpinnerScreen extends Screen {
    public final boolean isInstant;

    public ShowSpinnerScreen() {
        isInstant = false;
    }

    public ShowSpinnerScreen(boolean isInstant) {
        this.isInstant = isInstant;
    }
}
