package com.matthew.statefulbread.core.view

import android.app.Application
import android.util.Log
import com.google.android.material.color.DynamicColors
import com.matthew.statefulbread.core.TAG
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins

@HiltAndroidApp
class App : Application() {

    @Override
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        DynamicColors.applyToActivitiesIfAvailable(this)
        RxJavaPlugins.setErrorHandler {
            Log.e(TAG, it.localizedMessage!!)
            it.printStackTrace()
        }

    }

}
