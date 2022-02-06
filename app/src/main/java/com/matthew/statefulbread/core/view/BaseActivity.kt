package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.TAG
import com.matthew.statefulbread.core.toggleNightMode
import com.matthew.statefulbread.repo.IDataService
import com.matthew.statefulbread.repo.INavService
import com.matthew.statefulbread.repo.IPrefsService
import javax.inject.Inject

abstract class BaseActivity<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : AppCompatActivity() {

    @Inject lateinit var navService: INavService
    @Inject lateinit var dataService: IDataService
    @Inject lateinit var prefsService: IPrefsService
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

