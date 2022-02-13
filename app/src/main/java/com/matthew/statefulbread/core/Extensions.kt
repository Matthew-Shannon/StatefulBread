package com.matthew.statefulbread.core

import android.view.LayoutInflater

typealias Binder<B> = (LayoutInflater) -> B

val Any.TAG: String get() = "ASDF ${javaClass.simpleName}"