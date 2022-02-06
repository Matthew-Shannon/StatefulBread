package com.matthew.statefulbread.core.di

import android.app.Application
import com.matthew.statefulbread.repo.DataService
import com.matthew.statefulbread.repo.IDataService
import com.matthew.statefulbread.repo.IPrefsService
import com.matthew.statefulbread.repo.PrefsService
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
    fun providePrefs(app: Application): IPrefsService = PrefsService(app)

    @Provides
    @Singleton
    fun provideData(prefsService: IPrefsService): IDataService = DataService(prefsService)

}