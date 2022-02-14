package com.matthew.statefulbread.service

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

    override fun getOwnerEmail(): Single<String> = sharedPrefs()
        .map { it.getString("ownerEmail", "")!! }

    override fun setOwnerEmail(value: String): Completable = sharedPrefs()
        .map(SharedPreferences::edit)
        .map { it.putString("ownerEmail", value) }
        .map(Editor::apply)
        .ignoreElement()

    override fun getDayNightMode(): Single<Boolean> = sharedPrefs()
        .map { it.getBoolean("dayNightMode", false) }

    override fun setDayNightMode(value: Boolean): Completable = sharedPrefs()
        .map(SharedPreferences::edit)
        .map { it.putBoolean("dayNightMode", value) }
        .map(Editor::apply)
        .ignoreElement()

    override fun getAuthStatus(): Single<Boolean> = sharedPrefs()
        .map { it.getBoolean("authStatus", false) }

    override fun setAuthStatus(value: Boolean): Completable = sharedPrefs()
        .map(SharedPreferences::edit)
        .map { it.putBoolean("authStatus", value) }
        .map(Editor::apply)
        .ignoreElement()

    override fun getAge(): Single<Int> = sharedPrefs()
        .map { it.getInt("age", 0) }

    override fun setAge(value: Int): Completable = sharedPrefs()
        .map(SharedPreferences::edit)
        .map {  it.putInt("age", value) }
        .map(Editor::apply)
        .ignoreElement()

    override fun clear(): Completable = sharedPrefs()
        .map(SharedPreferences::edit)
        .map(Editor::clear)
        .map(Editor::apply)
        .ignoreElement()

    override fun getAll(): Single<Map<String, *>> = sharedPrefs()
        .map(SharedPreferences::getAll)
        .map(MutableMap<String, *>::orEmpty)

    private fun sharedPrefs(): Single<SharedPreferences> = Single.just(sharedPreferences)

}