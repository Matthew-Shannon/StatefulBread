package com.matthew.statefulbread.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs

abstract class BaseActivity<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : AppCompatActivity() {

    val binding: Binding by lazy { binder(layoutInflater) }
    val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    val nav: INav by lazy { App.castToApp(this).nav }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

}