package com.lyft.cityguide.models.dao;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @class DAOFactory
 * @brief
 */
@Module
public class DAOFactory {
    @Provides
    @Singleton
    public ISettingsDAO provideSettingsDAO(Context context) {
        return new SettingsDAO(context);
    }
}
