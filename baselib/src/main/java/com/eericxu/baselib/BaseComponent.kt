package com.eericxu.baselib

import android.animation.Animator
import android.content.Context
import android.content.res.Configuration
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast

open class BaseComponent(ctx: Context, layoutId: Int, datas: Map<String, Any>? = null) {
    companion object {
        val ORIENTATION_PORTRAIT = 1
        val ORIENTATION_LANDSCAPE = 2
        val ORIENTATION_SENSOR = 3

    }

    private val mView = LayoutInflater.from(ctx).inflate(layoutId, null)
    val view: View = mView
    var oneAtyHelper = (ctx as OneAty).getHelper()
    var attach: () -> Unit = {}
    var screenOrientation = ORIENTATION_PORTRAIT
    private var currentOrientation = ORIENTATION_PORTRAIT
    fun isLandscape(): Boolean {
        return currentOrientation == ORIENTATION_LANDSCAPE
    }

    init {
        mView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                onDetachedFromWindow()
                attach()
            }

            override fun onViewAttachedToWindow(v: View?) {
                onAttachToWindow()
            }

        })
    }

    /**
     * 移除过程中 移除动画*/
    var isRemoving: Boolean = false
    /**
     * 添加过程中 展示动画*/
    var isShowing: Boolean = false
    /**
     * 是否使用左滑返回*/
    var isSupportSwipeBack: Boolean = true
    private val views = SparseArray<View>()

    fun screenIsPortrait(): Boolean {
        return mView.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /*启动动画*/
    open fun animStart(): Animator? = null

    /*移除动画*/
    open fun animRemove(): Animator? = null

    /*隐藏动画*/
    open fun animHide(): Animator? = null

    /*显示动画*/
    open fun animShow(): Animator? = null

    /**
     * addView 之后*/
    open fun onStarted() {}

    /**
     * removeView 之前*/
    open fun onRemove() {}

    /**
     * 展示*/
    open fun onShow() {}

    /**
     * 隐藏*/
    open fun onHide() {}


    /**
     * 绑定到Window*/
    open fun onAttachToWindow() {

    }

    /**
     * 从Window 上移除*/
    open fun onDetachedFromWindow() {

    }

    /**
     * 状态栏高度*/
    val statusHeight by lazy {
        var result = 0
        val resourceId = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = ctx.resources.getDimensionPixelSize(resourceId)
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


    fun visible(visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }


}