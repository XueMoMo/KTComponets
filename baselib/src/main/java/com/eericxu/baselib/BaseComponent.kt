package com.eericxu.baselib

import android.animation.Animator
import android.content.Context
import android.content.res.Configuration
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.eericxu.baselib.manager.OneAtyHelper
import com.eericxu.baselib.utils.toOneAty

@Suppress("UNCHECKED_CAST")
open class BaseComponent(ctx: Context, layoutId: Int, datas: Map<String, Any>? = null) : View.OnAttachStateChangeListener {
    var mDatas = datas
    override fun onViewDetachedFromWindow(v: View?) {
        onDetachedFromWindow()
        attach()
    }

    override fun onViewAttachedToWindow(v: View?) {
        onAttachToWindow()
    }

    companion object {
        val ORIENTATION_PORTRAIT = 1
        val ORIENTATION_LANDSCAPE = 2
        val ORIENTATION_SENSOR = 3

    }

    private val mView = LayoutInflater.from(ctx).inflate(layoutId, null)
    val view: View = mView
    var oneAtyHelper: OneAtyHelper? = ctx.toOneAty()?.getHelper()
    var attach: () -> Unit = {}
    var screenOrientation = ORIENTATION_PORTRAIT
    private var currentOrientation = ORIENTATION_PORTRAIT
    fun isLandscape(): Boolean {
        return currentOrientation == ORIENTATION_LANDSCAPE
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

    /*隐藏动画 被新的覆盖时*/
    open fun animHide(): Animator? = null

    /*显示动画 移除最上层时*/
    open fun animShow(): Animator? = null

    /**
     * addView 之后*/
    open fun onStarted() {
        mView.addOnAttachStateChangeListener(this)
    }

    /**
     * removeView 之前*/
    open fun onRemove() {
        oneAtyHelper = null
        mDatas = null
        for (i in 0 until views.size()) {
            val keyAt = views.keyAt(i)
            val get = views.get(keyAt)
            get?.setOnClickListener(null)
            get?.setOnLongClickListener(null)
            get?.setOnTouchListener(null)
        }
        views.clear()
        mView.removeOnAttachStateChangeListener(this)
    }

    /**
     * 展示 再次展示*/
    open fun onShow() {}

    /**
     * 隐藏 被新的覆盖*/
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

    fun finish() {
        oneAtyHelper?.remove(this)
    }


}