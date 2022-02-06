package com.matthew.statefulbread.core

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.repo.IData
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.IPrefs
import javax.inject.Inject

abstract class BaseFragment<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : Fragment() {

    @Inject lateinit var nav: INav
    @Inject lateinit var data: IData
    @Inject lateinit var prefs: IPrefs
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