package com.matthew.statefulbread.core

import android.content.Context
import android.content.res.Configuration.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate.*

val Any.TAG: String get() = "ASDF ${javaClass.simpleName}".let { if (it.length <= 23) it else it.substring(0, 23) }

fun Context.hideKeyboard(view: View) = getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(view.windowToken, 0)

fun Context.toggleNightMode() = setDefaultNightMode(if (isNightMode()) MODE_NIGHT_NO else MODE_NIGHT_YES)

fun Context.isNightMode(): Boolean = when (resources.configuration.uiMode and UI_MODE_NIGHT_MASK) {
    UI_MODE_NIGHT_YES -> true
    UI_MODE_NIGHT_NO -> false
    else -> false
}