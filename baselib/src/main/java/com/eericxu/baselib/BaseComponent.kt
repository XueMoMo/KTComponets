package com.eericxu.baselib

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

open class BaseComponent(ctx: Context, layoutId: Int) {

    private val mView = LayoutInflater.from(ctx).inflate(layoutId, null)
    val view: View = mView
    var oneAtyHelper = (ctx as OneAty).getHelper()
    var isRemoving: Boolean = false
    private val views = SparseArray<View>()
    /**
     * addView 之后*/
    open fun onStart() {}

    /**
     * removeView 之后*/
    open fun onRemove() {}

    /**
     * 状态栏高度*/
    val statusHeight by lazy {
        var result = 0
        val resourceId = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = ctx.getResources().getDimensionPixelSize(resourceId)
        }
        result
    }


    fun <T : View?> findV(id: Int): T {
        val get = views.get(id)
        if (get != null)
            return get as T
        val viewById = mView.findViewById<View>(id)
        if (viewById != null)
            views.put(id, viewById)
        return viewById as T
    }


    fun toast(msg: String? = null, msgId: Int = 0) {
        if (msg == null && msgId == 0)
            return
        if (msg != null)
            Toast.makeText(mView.context, msg, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(mView.context, msgId, Toast.LENGTH_SHORT).show()
    }


}