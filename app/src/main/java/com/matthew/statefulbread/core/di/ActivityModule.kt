package com.matthew.statefulbread.core.di

import android.app.Activity
import com.matthew.statefulbread.repo.INavService
import com.matthew.statefulbread.repo.NavService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideNav(activity: Activity): INavService = NavService(activity)

}