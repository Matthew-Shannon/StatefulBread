package com.matthew.statefulbread.core.di

import android.app.Application
import com.matthew.statefulbread.repo.Data
import com.matthew.statefulbread.repo.IData
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.Prefs
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
    fun providePrefs(app: Application): IPrefs = Prefs(app)

    @Provides
    @Singleton
    fun provideData(prefs: IPrefs): IData = Data(prefs)

}