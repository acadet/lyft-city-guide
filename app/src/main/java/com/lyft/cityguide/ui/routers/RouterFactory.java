package com.lyft.cityguide.ui.routers;

import com.lyft.scoop.ScreenScooper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * RouterFactory
 * <p>
 */
@Module
public class RouterFactory {

    @Provides
    @Singleton
    @Named("app")
    public IRouter provideAppRouter() {
        return new AppRouter(new ScreenScooper());
    }

    @Provides
    @Singleton
    @Named("toast")
    public IRouter provideToastRouter() {
        return new ToastRouter(new ScreenScooper());
    }

    @Provides
    @Singleton
    @Named("spinner")
    public IRouter provideSpinnerRouter() {
        return new SpinnerRouter(new ScreenScooper());
    }

    @Provides
    @Singleton
    @Named("menu")
    public IRouter provideMenuRouter() {
        return new MenuRouter(new ScreenScooper());
    }
}
