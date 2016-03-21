package com.lyft.cityguide;

import com.lyft.cityguide.models.bll.BLLFactory;
import com.lyft.cityguide.models.bll.jobs.BLLJobFactory;
import com.lyft.cityguide.models.dao.DAOFactory;
import com.lyft.cityguide.models.services.google.distancematrix.api.GoogleDistanceMatrixAPIFactory;
import com.lyft.cityguide.models.services.google.distancematrix.jobs.GoogleDistanceMatrixServiceJobFactory;
import com.lyft.cityguide.models.services.google.place.api.GooglePlaceAPIFactory;
import com.lyft.cityguide.models.services.google.place.jobs.GooglePlaceServiceJobFactory;
import com.lyft.cityguide.ui.activities.BaseActivity;
import com.lyft.cityguide.ui.events.EventBusFactory;

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
    GooglePlaceAPIFactory.class,
    GoogleDistanceMatrixServiceJobFactory.class,
    GooglePlaceServiceJobFactory.class,
    BLLJobFactory.class,
    BLLFactory.class,
    EventBusFactory.class
})
public interface ApplicationComponent {
    void inject(BaseActivity activity);
}
