package com.eericxu.baselib.manager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class MRoot : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    var useSwipeBack = false
    private val xLeft by lazy { context.resources.displayMetrics.widthPixels / 10f }//左侧开始区域
    private var startMove = false
    var onStartMove: (() -> Unit)? = {}
    var onMove: ((Float) -> Unit)? = {}
    var onMoveEnd: (() -> Unit)? = {}

    var xD = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = super.onInterceptTouchEvent(ev)
        if (!useSwipeBack || intercept)
            return intercept
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                xD = ev.x
//                if (xD < xLeft)
//                    intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                val xM = ev.x
                val xOff = xM - xD
                if (xD < xLeft && xOff > 0) {
                    intercept = true
                }
            }
        }
        return intercept
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        var touch = super.onTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                xD = ev.x
                startMove = false
//                if (xD < xLeft)
//                    intercept = true
                touch = true
            }
            MotionEvent.ACTION_MOVE -> {
                val xM = ev.x
                val xOff = xM - xD
                if (xD < xLeft && xOff > 0) {
                    if (!startMove)
                        onStartMove?.invoke()
                    onMove?.invoke(xOff)
                    startMove = true
                    touch = true
                }
            }

            MotionEvent.ACTION_UP -> {
                startMove = false
                onMoveEnd?.invoke()
            }
        }
        return touch
    }
}