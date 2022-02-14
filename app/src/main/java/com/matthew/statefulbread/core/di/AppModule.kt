package com.matthew.statefulbread.core.di

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.matthew.statefulbread.service.*
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
    fun provideDarkMode(prefs: IPrefs): ITheme = Theme(prefs, AppCompatDelegate::setDefaultNightMode)

    @Provides
    @Singleton
    fun provideStorage(app: Application): IStorage = Storage(Room
        .databaseBuilder(app, AppDatabase::class.java, "StatefulBread")
        .build()
    )

    @Provides
    @Singleton
    fun providePrefs(app: Application): IPrefs = Prefs(app.getSharedPreferences("StatefulBread", Context.MODE_PRIVATE))

}