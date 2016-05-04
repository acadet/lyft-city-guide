package com.lyft.cityguide.dao;

import android.content.Context;
import android.content.SharedPreferences;

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
    SharedPreferences provideSettingsDAL(Context context) {
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    ISearchRangeSettingMapper provideSearchRangeSettingMapper() {
        return new SearchRangeSettingMapper();
    }

    @Provides
    @Singleton
    public ISettingsDAO provideSettingsDAO(SharedPreferences dal, ISearchRangeSettingMapper mapper) {
        return new SettingsDAO(dal, mapper);
    }
}
