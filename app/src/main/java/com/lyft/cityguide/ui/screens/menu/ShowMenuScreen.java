package com.lyft.cityguide.ui.screens.menu;

import com.lyft.cityguide.ui.controllers.menu.MenuController;
import com.lyft.scoop.Controller;
import com.lyft.scoop.EnterTransition;
import com.lyft.scoop.ExitTransition;
import com.lyft.scoop.Screen;
import com.lyft.scoop.transitions.BackwardSlideTransition;
import com.lyft.scoop.transitions.ForwardSlideTransition;

/**
 * ShowMenuScreen
 * <p>
 */
@Controller(MenuController.class)
@EnterTransition(BackwardSlideTransition.class)
@ExitTransition(ForwardSlideTransition.class)
public class ShowMenuScreen extends Screen {
}
