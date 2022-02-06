package com.matthew.statefulbread.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.repo.IData
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.IPrefs
import javax.inject.Inject

abstract class BaseActivity<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : AppCompatActivity() {

    @Inject lateinit var nav: INav
    @Inject lateinit var data: IData
    @Inject lateinit var prefs: IPrefs
    val binding: Binding by lazy { binder(layoutInflater) }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        binding.root.setOnLongClickListener { toggleNightMode(); true }
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

