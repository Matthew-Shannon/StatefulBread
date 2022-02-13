package com.matthew.statefulbread.repo

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface IPrefs {

    fun getOwnerEmail(): Single<String>
    fun setOwnerEmail(value: String): Completable

    fun getDayNightMode(): Single<Boolean>
    fun setDayNightMode(value: Boolean): Completable

    fun getAuthStatus(): Single<Boolean>
    fun setAuthStatus(value: Boolean): Completable

    fun getAge(): Single<Int>
    fun setAge(value: Int): Completable

    fun getAll(): Single<Map<String, *>>
    fun clear(): Completable
}

class Prefs(private val sharedPreferences: SharedPreferences) : IPrefs {

    override fun getOwnerEmail(): Single<String> = get { it.getString("ownerEmail", "")!! }
    override fun setOwnerEmail(value: String): Completable = edit { it.putString("ownerEmail", value) }

    override fun getDayNightMode(): Single<Boolean> = get { it.getBoolean("dayNightMode", false) }
    override fun setDayNightMode(value: Boolean) = edit { it.putBoolean("dayNightMode", value) }

    override fun getAuthStatus(): Single<Boolean> = get { it.getBoolean("authStatus", false) }
    override fun setAuthStatus(value: Boolean): Completable = edit { it.putBoolean("authStatus", value) }

    override fun getAge(): Single<Int> = get { it.getInt("age", 0) }
    override fun setAge(value: Int): Completable = edit { it.putInt("age", value) }

    override fun clear(): Completable = edit(Editor::clear)

    override fun getAll(): Single<Map<String, *>> = prefs()
        .map(SharedPreferences::getAll)
        .map(MutableMap<String, *>::orEmpty)


    private fun <T: Any> get(f: (SharedPreferences) -> T): Single<T> = prefs().map(f)

    private fun edit(f: (Editor) -> Editor): Completable = prefs()
        .map(SharedPreferences::edit)
        .map(f)
        .flatMapCompletable { Completable.fromAction(it::apply) }

    private fun prefs(): Single<SharedPreferences> = Single
        .just(sharedPreferences)
}