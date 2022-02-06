package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.TAG
import com.matthew.statefulbread.repo.IDataService
import com.matthew.statefulbread.repo.INavService
import com.matthew.statefulbread.repo.IPrefsService
import javax.inject.Inject

abstract class BaseFragment<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : Fragment() {

    @Inject lateinit var navService: INavService
    @Inject lateinit var dataService: IDataService
    @Inject lateinit var prefsService: IPrefsService
    val binding: Binding by lazy { binder(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?): View? {
        Log.d(TAG, "onCreateView")
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
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