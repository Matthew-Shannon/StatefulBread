package com.matthew.statefulbread.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate.*

val Any.TAG: String get() = "ASDF ${javaClass.simpleName}".let { if (it.length <= 23) it else it.substring(0, 23) }

fun Context.hideKeyboard(view: View) { getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(view.windowToken, 0) }

fun Context.setNightMode(flag: Boolean) { setDefaultNightMode(if (flag) MODE_NIGHT_YES else  MODE_NIGHT_NO) }
