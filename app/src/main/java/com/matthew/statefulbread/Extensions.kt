package com.matthew.statefulbread

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

val Any.TAG: String
    get() = "ASDF ${javaClass.simpleName}"
        .let { if (it.length <= 23) it else it.substring(0, 23) }

fun Context.hideKeyboard(view: View) =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)