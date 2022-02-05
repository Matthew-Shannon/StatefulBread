package com.matthew.statefulbread;

import android.app.Application;
import android.content.Context
import android.util.Log;
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.Nav
import com.matthew.statefulbread.service.Prefs


class App : Application() {

    lateinit var prefs: IPrefs
    lateinit var nav: INav

    @Override
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        prefs = Prefs.def(this)
        nav = Nav.def()
    }

    companion object {
        fun castToApp(context: Context): App = context.applicationContext as App
    }

}
