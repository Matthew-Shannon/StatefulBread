package com.matthew.statefulbread.core.di

import android.app.Activity
import com.matthew.statefulbread.R
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.MainNav
import com.matthew.statefulbread.repo.Nav
import com.matthew.statefulbread.repo.SplashNav
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @SplashNav
    @Provides
    fun provideSplashNav(activity: Activity): INav = Nav(activity, R.id.splash_container)

    @MainNav
    @Provides
    fun provideMainNav(activity: Activity): INav = Nav(activity, R.id.main_container)

}