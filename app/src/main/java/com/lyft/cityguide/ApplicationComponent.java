package com.lyft.cityguide;

import javax.inject.Singleton;

import dagger.Component;

/**
 * ApplicationComponent
 * <p>
 */
@Singleton
@Component(modules = {
    ApplicationModule.class
})
public interface ApplicationComponent {
    void inject(CityGuideApplication application);
}
