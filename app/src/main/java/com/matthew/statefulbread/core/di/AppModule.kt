package com.matthew.statefulbread.core.di

import android.app.Application
import com.matthew.statefulbread.core.Config
import com.matthew.statefulbread.repo.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDarkMode(prefs: IPrefs): ITheme = Theme(prefs)

    @Provides
    @Singleton
    fun provideStorage(app: Application): IStorage = Storage.def(app, Config.DATABASE_NAME)

    @Provides
    @Singleton
    fun providePrefs(app: Application): IPrefs = Prefs.def(app, Config.SHAREDPREFERENCES_NAME)

}