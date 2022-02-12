package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.TAG
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

abstract class BaseActivity<Binding: ViewBinding>(val binder: (LayoutInflater) -> Binding) : AppCompatActivity() {

    val binding: Binding by lazy { binder(layoutInflater) }
    val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(binding.root)
        Log.d(TAG, "onCreate")
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
        disposable.dispose()
    }

    fun <T: Any> sub(req: Observable<T>) = req.subscribe().addTo(disposable)
    fun <T: Any> sub(req: Single<T>) = req.subscribe().addTo(disposable)
    fun sub(req: Completable) = req.subscribe().addTo(disposable)
}

