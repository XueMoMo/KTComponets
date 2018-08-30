package com.eericxu.ktcomponets.uicomponet

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.widget.Button
import com.eericxu.baselib.BaseComponent
import com.eericxu.ktcomponets.R


class NextCpt(ctx: Context, index: Int) : BaseComponent(ctx, R.layout.layout_next) {
    init {
        val r = (Math.random() * 255).toInt()
        val g = (Math.random() * 255).toInt()
        val b = (Math.random() * 255).toInt()
        findV<Button>(R.id.tv_content).apply {
            text = "Next:$index"
            setOnClickListener {
                oneAtyHelper.start(NextCpt(ctx, index + 1))
            }
        }
        view.setBackgroundColor(Color.rgb(r, g, b))

        findV<Toolbar>(R.id.t_bar).apply {
            setPadding(0, statusHeight, 0, 0)
            title = "KTCoomponents:$index"
            setNavigationOnClickListener {
                oneAtyHelper.remove()
            }
        }
        findV<Button>(R.id.tv_input).setOnClickListener {
            oneAtyHelper.start(InputCpt(ctx))
        }
    }
}