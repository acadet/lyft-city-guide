package com.lyft.cityguide;

import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.jobs.BLLJobFactory;
import com.lyft.cityguide.models.bll.serializers.BLLSerializerFactory;
import com.lyft.cityguide.dao.DAOFactory;
import com.lyft.cityguide.services.google.distancematrix.GoogleDistanceMatrixServiceFactory;
import com.lyft.cityguide.services.google.distancematrix.GoogleDistanceMatrixAPIFactory;
import com.lyft.cityguide.services.google.distancematrix.GoogleDistanceMatrixServiceJobFactory;
import com.lyft.cityguide.services.google.place.GooglePlaceServiceFactory;
import com.lyft.cityguide.services.google.place.GooglePlaceAPIFactory;
import com.lyft.cityguide.services.google.place.GooglePlaceServiceJobFactory;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.components.MainUIContainer;
import com.lyft.cityguide.ui.controllers.BaseController;
import com.lyft.cityguide.ui.events.EventBusFactory;
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
    GoogleDistanceMatrixAPIFactory.class,
    GoogleDistanceMatrixServiceJobFactory.class,
    GoogleDistanceMatrixServiceFactory.class,
    GooglePlaceAPIFactory.class,
    GooglePlaceServiceJobFactory.class,
    GooglePlaceServiceFactory.class,
    BLLSerializerFactory.class,
    BLLJobFactory.class,
    BLLFactory.class,
    EventBusFactory.class,
    RouterFactory.class
})
public interface ApplicationComponent {
    void inject(BaseActivity activity);

    void inject(MainUIContainer container);

    void inject(BaseController controller);
}
