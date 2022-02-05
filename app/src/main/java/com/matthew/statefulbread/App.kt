package com.matthew.statefulbread;

import android.app.Application;
import android.content.Context
import android.util.Log;
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.Prefs


class App : Application() {

    lateinit var prefs: IPrefs

    @Override
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        prefs = Prefs.def(this)
    }

    companion object {
        fun castToApp(context: Context): App {
            return context.applicationContext as App
        }
    }

}
