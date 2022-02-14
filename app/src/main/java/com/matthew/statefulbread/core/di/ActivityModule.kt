package com.matthew.statefulbread.core.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.matthew.statefulbread.R
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.MainNav
import com.matthew.statefulbread.service.Nav
import com.matthew.statefulbread.service.SplashNav
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @SplashNav
    @Provides
    fun provideFragManagerSplash(activity: Activity): FragmentManager = (activity as AppCompatActivity).supportFragmentManager

    @MainNav
    @Provides
    fun provideFragManagerMain(activity: Activity): FragmentManager = (activity as AppCompatActivity).supportFragmentManager

    @SplashNav
    @Provides
    fun provideSplashNav(activity: Activity, @SplashNav fragmentManager: FragmentManager): INav = Nav(activity, fragmentManager, R.id.splash_container)

    @MainNav
    @Provides
    fun provideMainNav(activity: Activity, @MainNav fragmentManager: FragmentManager): INav = Nav(activity, fragmentManager, R.id.main_container)

}