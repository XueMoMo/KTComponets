package com.eericxu.baselib.manager

import android.animation.Animator
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.OneAty
import com.eericxu.baselib.anim.SimpleAnimLis
import java.util.*

/**
 * 管理UI组件*/
class ComponentManager(oneAty: OneAty) {
    private val stack = Stack<BaseComponent>()
    val root: FrameLayout = FrameLayout(oneAty as Context)

    init {
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        oneAty.getRoot().addView(root, 0, params)
    }

    fun lastElement(): BaseComponent? {
        if (stack.size > 0)
            return stack.lastElement()
        return null
    }

    /**
     * 启动一个组件*/
    fun start(component: BaseComponent, inAnim: Animator? = null, lastOutAnim: Animator? = null) {
        val lastElement = lastElement()
        stack.push(component)
        root.addView(component.view)
        component.onStart()
        inAnim?.apply {
            setTarget(component.view)
            start()
        }
        if (lastOutAnim == null || lastElement == null)
            enableComponent(lastElement, false)
        else {
            lastOutAnim.setTarget(lastElement.view)
            lastOutAnim.addListener(object : SimpleAnimLis() {
                override fun onAnimationEnd(animation: Animator?) {
                    enableComponent(lastElement, false)
                }
            })
            lastOutAnim.start()
        }
    }


    @Synchronized
    fun startSyn(component: BaseComponent) {
        start(component)
    }

    /**
     * 移除一个组件 不传时 将移除最后启动的那个组件*/
    fun remove(c: BaseComponent? = null, outAnim: Animator? = null, lastShowAnim: Animator? = null) {
        if (stack.size < 2)
            return
        val lastElement = stack.lastElement()
        val element = c ?: lastElement
        //上一个组件
        val lE = stack[stack.size - 2]
        enableComponent(lE, true)
        lastShowAnim?.apply {
            if (lE.isShowing)
                return
            lE.isShowing = true
            setTarget(lE.view)
            addListener(object : SimpleAnimLis() {
                override fun onAnimationEnd(animation: Animator?) {
                    lE.isShowing = false
                }
            })
            start()
        }

        //当前要删除的组件
        if (outAnim == null) {
            removeReal(element)
        } else {
            if (element.isRemoving) {
                return
            }
            element.isRemoving = true
            outAnim.setTarget(element.view)
            outAnim.addListener(object : SimpleAnimLis() {
                override fun onAnimationEnd(animation: Animator?) {
                    removeReal(element)
                    element.isRemoving = false
                }
            })
            outAnim.start()
        }
    }

    /**
     * 禁用组件*/
    private fun enableComponent(c: BaseComponent?, enable: Boolean) {
//        c?.view?.translationX = if (enable) 0f else 6000f
        c?.view?.visibility = if (enable) View.VISIBLE else View.GONE
    }

    private fun removeReal(element: BaseComponent) {
        root.removeView(element.view)
        element.onRemove()
        stack.remove(element)
    }

    @Synchronized
    fun removeSyn(c: BaseComponent? = null) {
        remove(c)
    }

    /**
     * 是否可以回退栈*/
    fun canBack(): Boolean {
        return stack.size > 1
    }

    fun clear() {

    }


}