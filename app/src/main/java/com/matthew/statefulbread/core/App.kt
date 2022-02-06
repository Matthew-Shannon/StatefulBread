package com.matthew.statefulbread.core;

import android.app.Application;
import android.content.Context
import android.util.Log;
import com.google.android.material.color.DynamicColors
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.Nav
import com.matthew.statefulbread.service.Prefs

class App : Application() {

    val prefs: IPrefs by lazy { Prefs.def(this) }
    val nav: INav by lazy { Nav.def() }

    @Override
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        DynamicColors.applyToActivitiesIfAvailable(this);
    }

    companion object {
        fun castToApp(context: Context): App = context.applicationContext as App
    }

}
