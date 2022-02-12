package com.matthew.statefulbread.repo

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.matthew.statefulbread.core.TAG
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ITheme {
    fun setDarkMode(status: Boolean): Completable
    fun getDarkMode(): Single<Boolean>
    fun toggleDarkMode(): Completable
    fun initDayNightMode(): Completable
}

class Theme constructor(private val prefs: IPrefs): ITheme {

    override fun setDarkMode(status: Boolean): Completable = Single.just(status)
        .map { if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO }
        .flatMapCompletable { Completable.fromAction { AppCompatDelegate.setDefaultNightMode(it) } }
        .subscribeOn(AndroidSchedulers.mainThread())

    override fun toggleDarkMode(): Completable {
        Log.d(TAG, "WOW : toggleDarkMode")
        return getDarkMode()
            .flatMapCompletable { prefs
                .setDarkMode(!it)
                .andThen(setDarkMode(!it))
            }
    }

    override fun initDayNightMode(): Completable = getDarkMode()
        .flatMapCompletable(::setDarkMode)


    override fun getDarkMode(): Single<Boolean> = prefs
        .getDarkMode()
        .observeOn(AndroidSchedulers.mainThread())

}
