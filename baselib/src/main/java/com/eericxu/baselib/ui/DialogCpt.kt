package com.eericxu.baselib.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.R


open class DialogCpt(ctx: Context) : BaseComponent(ctx, R.layout.layout_dialog_cpt) {

    init {
        for (id in arrayListOf(R.id.tv_title, R.id.tv_content, R.id.tv_left, R.id.tv_right)) {
            findV<View>(id).visibility = View.GONE
        }
        view.setOnClickListener { dismiss() }
        findV<View>(R.id.ll_layout).setOnClickListener { }
        isSupportSwipeBack = false
    }

    open fun title(t: String? = null, strId: Int = 0, size: Int = 0, color: Int = 0): DialogCpt {
        val tit = findV<TextView>(R.id.tv_title)
        tit.visibility = View.VISIBLE
        tit.text = t ?: view.resources.getString(strId)
        return this
    }

    open fun content(t: String? = null, strId: Int = 0): DialogCpt {
        val con = findV<TextView>(R.id.tv_content)
        con.visibility = View.VISIBLE
        con.text = t ?: view.resources.getString(strId)
        return this
    }

    open fun tvLeft(t: String? = "取消", strId: Int = 0, onClick: ((DialogCpt) -> Unit)? = null): DialogCpt {
        val tvL = findV<TextView>(R.id.tv_left)
        tvL.visibility = View.VISIBLE
        tvL.text = t ?: view.resources.getString(strId)
        tvL.setOnClickListener { onClick?.invoke(this) }
        return this
    }


    open fun tvRight(t: String? = "确定", strId: Int = 0, onClick: ((DialogCpt) -> Unit)? = null): DialogCpt {
        val tvR = findV<TextView>(R.id.tv_right)
        tvR.visibility = View.VISIBLE
        tvR.text = t ?: view.resources.getString(strId)
        tvR.setOnClickListener { onClick?.invoke(this) }
        return this
    }

    open fun outClickDismiss(enable: Boolean = true) {
        if (!enable)
            view.setOnClickListener { }
    }

    fun show() {
        oneAtyHelper.show(this)
    }

    fun dismiss() {
        oneAtyHelper.dismiss(this)
    }


}