package com.eericxu.baselib.utils

import android.content.Context
import android.content.ContextWrapper
import android.view.ContextThemeWrapper
import com.eericxu.baselib.OneAty


inline fun Context.toOneAty(): OneAty? {
    return when (this) {
        is OneAty -> this
        is ContextWrapper -> this.baseContext as? OneAty
        is ContextThemeWrapper -> this.baseContext as? OneAty
        else -> null
    }
}