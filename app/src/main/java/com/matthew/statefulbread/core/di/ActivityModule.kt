package com.matthew.statefulbread.core.di

import android.app.Activity
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.Nav
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideNav(activity: Activity): INav = Nav(activity)

}