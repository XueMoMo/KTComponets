package com.eericxu.ktcomponets.uicomponet

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.Interpolator
import android.widget.TextView
import com.eericxu.baselib.BaseComponent
import com.eericxu.cslibrary.*
import com.eericxu.cslibrary.anim.CSViewAnim
import com.eericxu.cslibrary.anim.ViewAnim
import com.eericxu.cslibrary.keyparms.CSKeyParm
import com.eericxu.cslibrary.keyparms.KeyParm
import com.eericxu.ktcomponets.R

class ShareAnimDetailCpt(ctx: Context, val datas: Map<String, Any> = mutableMapOf())
    : BaseComponent(ctx, R.layout.layout_share_anim_del, datas) {


    fun createAnimator(isStart: Boolean, keyParm: KeyParm, v: View,
                       duration: Long = 600,
                       interpolator: Interpolator = OffsetInterpolator()
    ): Animator {
        val anim: Animator = when (v) {
            is CSInterface -> CSViewAnim(isStart, v, keyParm as CSKeyParm, CSKeyParm(keyParm.key, v.rectInWindow(), CSParms()))
            else -> ViewAnim(isStart, v, keyParm, KeyParm(keyParm.key, v.rectInWindow()))
        }
        anim.duration = duration
        anim.interpolator = interpolator
        return anim
    }

    init {
        findV<TextView>(R.id.tv_content).apply {
            val builder = StringBuilder()
            for (i in 0..100) {
                builder.append("卡时间段开奖\n")
            }
            text = builder
        }
    }
    override fun onStarted() {

    }

    override fun animStart(): Animator? {
        val set = AnimatorSet()
        set.playTogether(
                createAnimator(true, datas["csLayout"] as CSKeyParm, view as CSLayout),
                createAnimator(true, datas["ivCover"] as KeyParm, findV(R.id.iv_cover)),
                createAnimator(true, datas["tvTit"] as KeyParm, findV(R.id.tv_tit))
        )
        return set
    }

    override fun animRemove(): Animator? {
        val set = AnimatorSet()
        set.playTogether(
                createAnimator(false, datas["csLayout"] as CSKeyParm, view as CSLayout),
                createAnimator(false, datas["ivCover"] as KeyParm, findV(R.id.iv_cover)),
                createAnimator(false, datas["tvTit"] as KeyParm, findV(R.id.tv_tit))
        )
        return set
    }

}