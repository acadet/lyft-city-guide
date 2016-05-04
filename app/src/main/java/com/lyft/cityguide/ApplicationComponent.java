package com.lyft.cityguide;

import com.lyft.cityguide.bll.BLLFactory;
import com.lyft.cityguide.dao.DAOFactory;
import com.lyft.cityguide.services.google.distancematrix.GoogleDistanceMatrixServiceFactory;
import com.lyft.cityguide.services.google.place.GooglePlaceServiceFactory;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.containers.MainUIContainer;
import com.lyft.cityguide.ui.containers.SpinnerUIContainer;
import com.lyft.cityguide.ui.containers.ToastUIContainer;
import com.lyft.cityguide.ui.controllers.BaseController;
import com.lyft.cityguide.ui.controllers.spinner.SpinnerController;
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
    void inject(BaseActivity activity);

    void inject(MainUIContainer mainUIContainer);

    void inject(ToastUIContainer toastUIContainer);

    void inject(SpinnerUIContainer spinnerUIContainer);

    void inject(BaseController baseController);

    void inject(SpinnerController spinnerController);

    void inject(ToastController toastController);
}
