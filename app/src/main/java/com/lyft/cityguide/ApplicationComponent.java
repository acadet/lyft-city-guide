package com.lyft.cityguide;

import com.lyft.cityguide.bll.BLLFactory;
import com.lyft.cityguide.dao.DAOFactory;
import com.lyft.cityguide.services.google.distancematrix.GoogleDistanceMatrixServiceFactory;
import com.lyft.cityguide.services.google.place.GooglePlaceServiceFactory;
import com.lyft.cityguide.ui.UIComponent;
import com.lyft.cityguide.ui.UIModule;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.containers.MainUIContainer;
import com.lyft.cityguide.ui.containers.MenuUIContainer;
import com.lyft.cityguide.ui.containers.SpinnerUIContainer;
import com.lyft.cityguide.ui.containers.ToastUIContainer;
import com.lyft.cityguide.ui.controllers.BaseController;
import com.lyft.cityguide.ui.controllers.menu.MenuController;
import com.lyft.cityguide.ui.controllers.toast.ToastController;
import com.lyft.cityguide.ui.routers.RouterFactory;

import javax.inject.Singleton;

import dagger.Component;

/**
 * ApplicationComponent
 * <p>
 */
@Singleton
@Component(modules = {
    ApplicationModule.class,
    DAOFactory.class,
    GoogleDistanceMatrixServiceFactory.class,
    GooglePlaceServiceFactory.class,
    BLLFactory.class,
    RouterFactory.class
})
public interface ApplicationComponent {
    UIComponent uiComponent(UIModule uiModule);

    void inject(BaseActivity activity);

    void inject(MainUIContainer mainUIContainer);

    void inject(ToastUIContainer toastUIContainer);

    void inject(SpinnerUIContainer spinnerUIContainer);

    void inject(MenuUIContainer menuUIContainer);

    void inject(BaseController baseController);

    void inject(ToastController toastController);

    void inject(MenuController menuController);
}
