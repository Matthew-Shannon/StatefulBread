package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.Binder
import com.matthew.statefulbread.core.TAG
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment<Binding: ViewBinding>(val binder: Binder<Binding>) : Fragment() {

    val binding: Binding by lazy { binder(layoutInflater) }
    val disposable: CompositeDisposable by lazy { CompositeDisposable() }

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
        Log.d("TAG", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onDestroyView() {
        disposable.clear()
        super.onDestroyView()
    }

}