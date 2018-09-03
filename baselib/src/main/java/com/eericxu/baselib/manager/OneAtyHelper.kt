package com.eericxu.baselib.manager

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.OneAty
import com.eericxu.baselib.ui.DialogCpt

/*单Activity 的实现帮助类*/
class OneAtyHelper(oneAty: OneAty) {
    private val cManager = ComponentManager(oneAty)
    private val mRoot = oneAty.getRoot()
    private val sWidth = mRoot.resources.displayMetrics.widthPixels.toFloat()
    private val sHeight = mRoot.resources.displayMetrics.heightPixels.toFloat()
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


    private var screenOrientation: Int = 1
    private fun screenIsPortrait(): Boolean {
        return screenOrientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun animLength(): Float {
        return if (screenIsPortrait()) sWidth else sHeight
    }

    private fun defInAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, animLength(), 0f).setDuration(300)
    }

    private fun defOutAnim(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, animLength()).setDuration(300)
    }

    private fun defLastOut(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, 0f, -animLength()).setDuration(300)
    }

    private fun defLstShow(): Animator? {
        return ObjectAnimator.ofFloat(null, View.TRANSLATION_X, -animLength(), 0f).setDuration(300)
    }

    /**
     * 启动一个组件*/
    fun start(component: BaseComponent, anim: Animator? = defInAnim(), lastOutAnim: Animator? = defLastOut()) {
        cManager.start(component, anim, lastOutAnim)
    }

    /**
     * 移除一个组件 不传时 将移除最后启动的那个组件*/
    fun remove(component: BaseComponent? = null, anim: Animator? = defOutAnim(), lastShowAnim: Animator? = defLstShow()) {
        val element = cManager.lastElement()

        if (element != null && element is DialogCpt)
            dismiss(element)
        else
            cManager.remove(component, anim, lastShowAnim)
    }

    fun onConfigurationChanged(newConfig: Configuration?) {
        newConfig?.apply {
            screenOrientation = orientation
            Log.i("OneAty:", "orientation:${newConfig.orientation}  w:$sWidth   h:$sHeight")
        }
    }

    fun show(dialogCpt: DialogCpt) {
        val inAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 0f, 1f)
        inAnim.duration = 200
        cManager.start(dialogCpt, inAnim, null,false)
    }

    fun dismiss(dialogCpt: DialogCpt) {
        val outAnim = ObjectAnimator.ofFloat(null, View.ALPHA, 1f, 0f)
        outAnim.duration = 200
        cManager.remove(dialogCpt, outAnim, null)
    }


}