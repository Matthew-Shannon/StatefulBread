package com.matthew.statefulbread.repo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Observable

interface IPrefsService {

    fun getName(): String
    fun setName(value: String)

    fun getEmail(): String
    fun setEmail(value: String)

    fun getZipCode(): String
    fun setZipCode(value: String)

    fun getPassword(): String
    fun setPassword(value: String)

    fun getDarkMode(): Boolean
    fun getDarkModeSingle(): Observable<Boolean>
    fun setDarkMode(value: Boolean)

    fun clear()
}

class PrefsService(private val context: Context) : IPrefsService {

    private val sharedPrefs: SharedPreferences by lazy { context.getSharedPreferences("Bread", MODE_PRIVATE) }

    override fun getName(): String = sharedPrefs.getString("name", "")!!
    override fun setName(value: String) = sharedPrefs.edit().putString("name", value).apply()

    override fun getEmail(): String = sharedPrefs.getString("email", "")!!
    override fun setEmail(value: String) = sharedPrefs.edit().putString("email", value).apply()

    override fun getZipCode(): String = sharedPrefs.getString("zipCode", "")!!
    override fun setZipCode(value: String) = sharedPrefs.edit().putString("zipCode", value).apply()

    override fun getPassword(): String = sharedPrefs.getString("password", "")!!
    override fun setPassword(value: String) = sharedPrefs.edit().putString("password", value).apply()

    override fun getDarkMode(): Boolean = sharedPrefs.getBoolean("darkMode", false)
    override fun getDarkModeSingle(): Observable<Boolean> = Observable.just(getDarkMode())
    override fun setDarkMode(value: Boolean) = sharedPrefs.edit().putBoolean("darkMode", value).apply()

    override fun clear() = sharedPrefs.edit().clear().apply()

}