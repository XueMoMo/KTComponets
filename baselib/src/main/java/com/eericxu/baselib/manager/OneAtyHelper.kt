package com.eericxu.baselib.manager

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.OneAty

/*单Activity 的实现帮助类*/
class OneAtyHelper(val oneAty: OneAty) {
    val cManager = ComponentManager(oneAty)
    fun onCreateAty(savedInstanceState: Bundle?) {
        cManager.clear()
    }

    fun onStart() {

    }

    fun onResume() {

    }

    fun onPause() {}

    fun onStop() {}


    fun onRestart() {

    }


    fun onDestroy() {}
    /**
     * 回退栈
     * return  true 回退成功  false 回退失败*/
    fun back(): Boolean {
        if (cManager.canBack()) {
            remove()
            return true
        }
        return false
    }


    private val sWidth by lazy { (oneAty as Context).resources.displayMetrics.widthPixels.toFloat() }

    private fun defInAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, sWidth, 0f).setDuration(300)
    }

    private fun defOutAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, sWidth).setDuration(300)
    }

    private fun defLastOut(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, -sWidth).setDuration(300)
    }

    private fun defLstShow(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, -sWidth, 0f).setDuration(300)
    }

    /**
     * 启动一个组件*/
    fun start(component: BaseComponent, anim: Animator? = defInAnim(), lastOutAnim: Animator? = defLastOut()) {
        cManager.start(component, anim, lastOutAnim)
    }

    /**
     * 移除一个组件 不传时 将移除最后启动的那个组件*/
    fun remove(component: BaseComponent? = null, anim: Animator? = defOutAnim(), lastShowAnim: Animator? = defLstShow()) {
        cManager.remove(component, anim, lastShowAnim)
    }


}