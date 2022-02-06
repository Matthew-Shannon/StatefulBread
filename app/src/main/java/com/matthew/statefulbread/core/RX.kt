package com.matthew.statefulbread.core

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers


fun <T : Any> setThreadsSingle(): SingleTransformer<T, T> = SingleTransformer { it
    .subscribeOn(Schedulers.io())
    .compose(singleOnMain())
}

fun <T : Any> singleOnMain(): SingleTransformer<T, T> = SingleTransformer { it
    .observeOn(AndroidSchedulers.mainThread())
}

fun <T : Any> setThreadsObservable(): ObservableTransformer<T, T> = ObservableTransformer { it
    .subscribeOn(Schedulers.io())
    .compose(observableOnMain())
}

fun <T : Any> observableOnMain(): ObservableTransformer<T, T> = ObservableTransformer { it
    .observeOn(AndroidSchedulers.mainThread())
}