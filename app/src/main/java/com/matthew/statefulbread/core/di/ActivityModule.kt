package com.matthew.statefulbread.core.di

import android.app.Activity
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.core.view.MainNav
import com.matthew.statefulbread.core.view.Nav
import com.matthew.statefulbread.core.view.SplashNav
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