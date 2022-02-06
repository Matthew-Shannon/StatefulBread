package com.matthew.statefulbread.core

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    @Override
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}
