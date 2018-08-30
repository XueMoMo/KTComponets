package com.eericxu.baselib

import android.view.ViewGroup
import com.eericxu.baselib.manager.OneAtyHelper

interface OneAty {
    fun getRoot(): ViewGroup
    fun getHelper(): OneAtyHelper
}