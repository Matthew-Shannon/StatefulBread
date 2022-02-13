package com.matthew.statefulbread.core.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.matthew.statefulbread.core.Binder
import com.matthew.statefulbread.core.TAG
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseActivity<Binding: ViewBinding>(val binder: Binder<Binding>) : AppCompatActivity() {

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

}


class DisposableManager {
    private val compositeDisposable = CompositeDisposable()
    fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun dispose() {
        compositeDisposable.clear()
    }
}