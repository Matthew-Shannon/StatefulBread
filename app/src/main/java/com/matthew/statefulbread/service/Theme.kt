package com.matthew.statefulbread.service

import androidx.appcompat.app.AppCompatDelegate
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.function.Consumer

interface ITheme {
    fun setDayNightMode(status: Boolean): Completable
    fun toggleDayNightMode(): Completable
    fun initDayNightMode(): Completable
}

class Theme constructor(private val prefs: IPrefs, private val onSet: Consumer<Int>): ITheme {

    override fun setDayNightMode(status: Boolean): Completable = Single.just(status)
        .map { if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO }
        .flatMapCompletable { Completable.fromAction { onSet.accept(it) } }

    override fun toggleDayNightMode(): Completable  = prefs.getDayNightMode()
        .flatMapCompletable { prefs
            .setDayNightMode(!it)
            .andThen(setDayNightMode(!it))
        }

    override fun initDayNightMode(): Completable = prefs.getDayNightMode()
        .flatMapCompletable(::setDayNightMode)
}
