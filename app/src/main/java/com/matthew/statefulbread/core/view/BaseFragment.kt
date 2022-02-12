package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.TAG
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class BaseFragment<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : Fragment() {

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

    fun <T: Any> sub(req: Observable<T>) = req.subscribe().addTo(disposable)
    fun <T: Any> sub(req: Single<T>) = req.subscribe().addTo(disposable)
    fun sub(req: Completable) = req.subscribe().addTo(disposable)
}