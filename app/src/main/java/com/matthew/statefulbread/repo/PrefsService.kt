package com.matthew.statefulbread.repo

import android.content.Context
import android.content.SharedPreferences


interface IPrefsService {

    fun getString(key: String): String
    fun setString(key: String, value: String)

    fun getBoolean(key: String): Boolean
    fun setBoolean(key: String, value: Boolean)

    fun getInt(key: String): Int
    fun setInt(key: String, value: Int)

    fun clear()
}

class PrefsService(private val context: Context) : IPrefsService {

    private val sharedPrefs: SharedPreferences by lazy { context.getSharedPreferences("Bread", Context.MODE_PRIVATE) }

    override fun getString(key: String): String = sharedPrefs.getString(key, "") ?: ""
    override fun setString(key: String, value: String) = sharedPrefs.edit().putString(key, value).apply()

    override fun getBoolean(key: String): Boolean = sharedPrefs.getBoolean(key, false)
    override fun setBoolean(key: String, value: Boolean) = sharedPrefs.edit().putBoolean(key, value).apply()

    override fun getInt(key: String): Int = sharedPrefs.getInt(key, 0)
    override fun setInt(key: String, value: Int) = sharedPrefs.edit().putInt(key, value).apply()

    override fun clear() = sharedPrefs.edit().clear().apply()

}