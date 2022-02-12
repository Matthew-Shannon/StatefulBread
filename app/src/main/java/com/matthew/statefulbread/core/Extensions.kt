package com.matthew.statefulbread.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import io.reactivex.rxjava3.core.Completable

val Any.TAG: String get() = "ASDF ${javaClass.simpleName}".let { if (it.length <= 23) it else it.substring(0, 23) }

fun Context.hideKeyboard(view: View) { getSystemService(InputMethodManager::class.java).hideSoftInputFromWindow(view.windowToken, 0) }

fun Fragment.hideKeyboard(view: View) { activity?.hideKeyboard(view) }


fun TextInputEditText.getValue(): String = text?.trim()?.toString().orEmpty()

fun View.onClick(): Completable = this.clicks().flatMapCompletable { _ -> Completable.complete() }
fun View.onLongClick(): Completable = this.longClicks().flatMapCompletable { _ -> Completable.complete() }