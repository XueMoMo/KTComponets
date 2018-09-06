package com.eericxu.baselib.manager

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.OneAty
import com.eericxu.baselib.R
import com.eericxu.baselib.anim.SimpleAnimLis

/**
 * 管理UI组件*/
class ComponentManager(oneAty: OneAty) {
    /**
     * 保存组件引用 */
    private val mStack = ArrayList<BaseComponent>()
    /**
     * 所有组件的父View */
    private val mRoot: MRoot = MRoot(oneAty as Context)


    init {
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        oneAty.getRoot().addView(mRoot, 0, params)
        enableSwipeBack(true)
    }

    /**
     * 当前顶层组件*/
    fun lastElement(): BaseComponent? {
        if (mStack.size > 0)
            return mStack.last()
        return null
    }

    /**
     * 启动一个组件*/
    fun start(component: BaseComponent, inAnim: Animator? = null, lastOutAnim: Animator? = null, hideLast: Boolean = true) {
        val lastElement = lastElement()
        mStack.add(component)
        mRoot.addView(component.view)
        component.view.alpha = 0f
        component.view.post {
            //启动动画
            component.view.alpha = 1f
            val animStart = component.animStart()
            inAnim?.setTarget(component.view)
            val startAnim = animStart ?: inAnim
            if (startAnim == null) {
                component.onStarted()
            } else {
                startAnim.addListener(object : SimpleAnimLis() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        component.onStarted()
                    }
                })
                startAnim.start()
            }

            //隐藏动画（上一个）
            if (lastElement != null) {
                lastOutAnim?.setTarget(lastElement.view)
                val outAnim = lastElement.animHide() ?: lastOutAnim
                if (outAnim == null) {
                    lastElement.onHide()
                    enableComponent(lastElement, !hideLast)
                } else {
                    outAnim.addListener(object : SimpleAnimLis() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            lastElement.onHide()
                            enableComponent(lastElement, !hideLast)
                        }
                    })
                    outAnim.start()
                }
            }
        }

    }


    @Synchronized //未完成
    fun startSyn(component: BaseComponent) {
        start(component)
    }

    /**
     * 移除一个组件 不传时 将移除最后启动的那个组件*/
    fun remove(c: BaseComponent? = null, outAnim: Animator? = null, lastShowAnim: Animator? = null) {
        if (mStack.size < 1)
            return

        val top = mStack.last()
        val element = c ?: top

        //当删除的不是最顶层的时
        if (c != null && top != c) {
            removeReal(c)
            return
        }
        //Top之下的一个组件
        val lE = mStack[mStack.size - 2]


        //显示动画（上一个）
        enableComponent(lE, true)
        lastShowAnim?.setTarget(lE.view)
        val animShow = lE.animShow() ?: lastShowAnim
        if (animShow == null) {
            lE.onShow()
        } else {
            animShow.apply {
                if (lE.isShowing)
                    return
                lE.isShowing = true
                addListener(object : SimpleAnimLis() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        lE.isShowing = false
                        lE.onShow()
                    }
                })
                start()
            }
        }

        //移除动画
        outAnim?.setTarget(element.view)
        val animRemove = element.animRemove() ?: outAnim
        if (animRemove == null) {
            removeReal(element)
        } else {
            if (element.isRemoving) {
                return
            }
            element.isRemoving = true
            animRemove.addListener(object : SimpleAnimLis() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    element.isRemoving = false
                    removeReal(element)
                }
            })
            animRemove.start()
        }
    }

    /**
     * 禁用组件*/
    fun enableComponent(c: BaseComponent?, enable: Boolean) {
//        c?.view?.translationX = if (enable) 0f else 6000f
        c?.view?.visibility = if (enable) View.VISIBLE else View.GONE
    }

    /**
     * 从父View中移除组件*/
    private fun removeReal(element: BaseComponent) {
        val last = if (mStack.size > 1) mStack[mStack.size - 2] else null
        element.onRemove()
        mRoot.removeView(element.view)
        mStack.remove(element)
//        System.runFinalization()
//        System.gc()
    }

    @Synchronized //未完成
    fun removeSyn(c: BaseComponent? = null) {
        remove(c)
    }

    /**
     * 是否可以回退栈*/
    fun canBack(): Boolean {
        return mStack.size > 1
    }

    /**
     * 清空*/
    fun clear() {
        mRoot.removeAllViews()
    }


    private var xD = 0f //Down时的X坐标
    private val xLeft by lazy { mRoot.context.resources.displayMetrics.widthPixels / 10f }//左侧开始区域
    private var startMove = false//开始滑动的标志
    private var moveTarget: BaseComponent? = null//当前需要滑动的组件
    private var lastTarget: BaseComponent? = null//要返回到的组件
    private val shader by lazy { mRoot.context.resources.getDimensionPixelSize(R.dimen.swipeback_left_shade).toFloat() }

    /**
     * 左滑返回*/
    @SuppressLint("ClickableViewAccessibility")
    fun enableSwipeBack(enable: Boolean) {
        if (enable) {
            mRoot.useSwipeBack = true
            mRoot.onStartMove = { startMoveTarget() }
            mRoot.onMove = { moveTarget(it) }
            mRoot.onMoveEnd = { endMoveTarget() }
        } else {
            mRoot.useSwipeBack = false
        }

    }

    /**
     * 结束滑动时*/
    private fun endMoveTarget() {
        startMove = false
        val target = moveTarget ?: return
        val transX = target.view.translationX
        val end =
                if (transX > (target.view.width / 3)) {
                    target.view.width.toFloat()
                } else {
                    0f
                }

        val anim = ValueAnimator.ofFloat(transX, end)
        anim.duration = 200
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            target.view.translationX = value
            val lastView = lastTarget?.view ?: return@addUpdateListener
            lastView.translationX = (value - lastView.width) / 2
        }
        anim.addListener(object : SimpleAnimLis() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (end == 0f) {
                    enableComponent(lastTarget, false)
                    moveTarget?.view?.translationZ = 0f
                } else {
                    moveTarget?.let { removeReal(it) }
                }
                moveTarget = null
                lastTarget = null
            }
        })
        anim.start()
    }

    /**
     * 开始滑动时*/
    private fun startMoveTarget() {
        if (mStack.size > 1) {
            val lastElement = mStack.last()
            if (lastElement.isSupportSwipeBack) {
                moveTarget = lastElement
                lastElement.view.translationZ = shader
                val component = mStack[mStack.size - 2]
                component.view.visibility = View.VISIBLE
                component.view.translationX = -component.view.width * 0.5f
                lastTarget = component
            }
        }
    }

    /**
     * 滑动过程中*/
    private fun moveTarget(offX: Float) {
        moveTarget?.view?.translationX = offX
        lastTarget?.apply { view.translationX = (offX - view.width) / 2 }
    }

}