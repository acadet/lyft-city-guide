package com.lyft.cityguide;

import com.lyft.cityguide.models.dao.DAOFactory;
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
    DAOFactory.class
})
public interface ApplicationComponent {
    void inject(BaseActivity activity);
}
