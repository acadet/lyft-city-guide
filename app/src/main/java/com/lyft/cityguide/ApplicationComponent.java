package com.lyft.cityguide;

import com.lyft.cityguide.models.dao.DAOFactory;
import com.lyft.cityguide.models.services.google.distancematrix.api.GoogleDistanceMatrixAPIFactory;
import com.lyft.cityguide.models.services.google.place.api.GooglePlaceAPIFactory;
import com.lyft.cityguide.ui.activities.BaseActivity;

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
    GooglePlaceAPIFactory.class
})
public interface ApplicationComponent {
    void inject(BaseActivity activity);
}
