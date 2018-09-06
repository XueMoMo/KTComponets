package com.eericxu.baselib.anim

import android.animation.Animator
import android.animation.ValueAnimator

open class SimpleAnimLis : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
        animation?.removeAllListeners()
        (animation as? ValueAnimator)?.removeAllUpdateListeners()
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationStart(animation: Animator?) {
    }

}