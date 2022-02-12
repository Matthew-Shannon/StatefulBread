package com.matthew.statefulbread.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

interface IPrefs {

    fun getOwnerID(): Single<Int>
    fun setOwnerID(value: Int): Completable

    fun getDarkMode(): Single<Boolean>
    fun setDarkMode(value: Boolean): Completable

    fun getAuthStatus(): Single<Boolean>
    fun setAuthStatus(value: Boolean): Completable

    fun getAge(): Single<Int>
    fun setAge(value: Int): Completable

    fun getAll(): Single<Map<String, *>>
    fun clear(): Completable
}

class Prefs(private val sharedPreferences: SharedPreferences) : IPrefs {

    override fun getOwnerID(): Single<Int> = get { it.getInt("ownerId", 0) }
    override fun setOwnerID(value: Int): Completable = edit { it.putInt("ownerId", value) }

    override fun getDarkMode(): Single<Boolean> = get { it.getBoolean("darkMode", false) }
    override fun setDarkMode(value: Boolean) = edit { it.putBoolean("darkMode", value) }

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
        .map(f).flatMapCompletable { Completable.fromAction(it::apply) }

    private fun prefs(): Single<SharedPreferences> = Single
        .just(sharedPreferences)
        .observeOn(Schedulers.io())

    companion object {
        fun def(context: Context, name: String): IPrefs = Prefs(context.getSharedPreferences(name, MODE_PRIVATE))
    }

}