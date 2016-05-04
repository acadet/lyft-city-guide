package com.lyft.cityguide.ui.screens.toast;

import com.lyft.cityguide.ui.controllers.toast.ToastController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.EnterTransition;
import com.lyft.scoop.ExitTransition;
import com.lyft.scoop.Screen;
import com.lyft.scoop.transitions.DownwardSlideTransition;
import com.lyft.scoop.transitions.UpwardSlideTransition;

/**
 * ConfirmScreen
 * <p>
 */
@Controller(ToastController.class)
@EnterTransition(DownwardSlideTransition.class)
@ExitTransition(UpwardSlideTransition.class)
public class ConfirmScreen extends Screen {
    public final String message;

    public ConfirmScreen(String message) {
        this.message = message;
    }
}
