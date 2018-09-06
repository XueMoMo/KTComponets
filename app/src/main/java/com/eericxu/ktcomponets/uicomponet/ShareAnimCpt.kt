package com.eericxu.ktcomponets.uicomponet

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.eericxu.baselib.BaseComponent
import com.eericxu.cslibrary.CSLayout
import com.eericxu.cslibrary.ScaleViewGesture
import com.eericxu.cslibrary.createKeyParm
import com.eericxu.ktcomponets.R

class ShareAnimCpt(ctx: Context) : BaseComponent(ctx, R.layout.layout_share_anim) {


    init {
        isSupportSwipeBack = false
    }

    override fun onStarted() {
        super.onStarted()
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        val top = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, view.resources.displayMetrics).toInt()
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                parent?.getChildAdapterPosition(view)?.apply {
                    if (this == 0)
                        return@apply
                    else
                        outRect?.top = -top
                }
            }
        })
        val ada = object : RecyclerView.Adapter<BaseViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_share_anim, parent, false))
            }

            override fun getItemCount(): Int {
                return 100
            }

            override fun onBindViewHolder(helper: BaseViewHolder, position: Int) {
                val csL = helper.itemView as CSLayout
                ScaleViewGesture(helper.itemView.context)
                        .bindToView(csL, csL)?.onClick = {
                    val map = mutableMapOf<String, Any>()
                    createKeyParm("csLayout", csL).apply { map[key] = this }
                    createKeyParm("ivCover", helper.getView(R.id.iv_cover)).apply { map[key] = this }
                    createKeyParm("tvTit", helper.getView(R.id.tv_tit)).apply { map[key] = this }
                    oneAtyHelper?.startT<ShareAnimDetailCpt>(map, null, null, false)
                }

            }

        }
        recyclerView.adapter = ada
    }

    override fun animShow(): Animator? {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 1f).setDuration(100)
    }

}