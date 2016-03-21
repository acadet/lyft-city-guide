package com.lyft.cityguide.ui.screens;

import com.lyft.cityguide.ui.controllers.SettingsController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.EnterTransition;
import com.lyft.scoop.ExitTransition;
import com.lyft.scoop.Screen;
import com.lyft.scoop.transitions.FadeTransition;

/**
 * SettingsScreen
 * <p>
 */
@Controller(SettingsController.class)
@EnterTransition(FadeTransition.class)
@ExitTransition(FadeTransition.class)
public class SettingsScreen extends Screen {
}
