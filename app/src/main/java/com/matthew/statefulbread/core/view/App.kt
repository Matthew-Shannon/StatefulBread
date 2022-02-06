package com.matthew.statefulbread.core.view

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors
import com.matthew.statefulbread.core.TAG
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
