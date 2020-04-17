package com.eericxu.ktcomponets.uicomponet

import android.content.Context
import android.graphics.Color
import android.widget.Button
import android.widget.Toolbar
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.ui.DialogCpt
import com.eericxu.ktcomponets.R


class NextCpt(ctx: Context, datas: MutableMap<String, Any>? = null) : BaseComponent(ctx, R.layout.layout_next,datas) {

    val index = mDatas?.get("index") as Int
    init {

        val r = (Math.random() * 255).toInt()
        val g = (Math.random() * 255).toInt()
        val b = (Math.random() * 255).toInt()

        view.setBackgroundColor(Color.rgb(r, g, b))
        findV<Toolbar>(R.id.t_bar).setPadding(0, statusHeight, 0, 0)
        findV<Toolbar>(R.id.t_bar).title =  "KTCoomponents:${index}"

    }

    override fun onStarted() {
        super.onStarted()

        findV<Button>(R.id.tv_content).setOnClickListener {
            oneAtyHelper?.startT<NextCpt>(mutableMapOf("index" to index + 1))
        }
        findV<Toolbar>(R.id.t_bar).setNavigationOnClickListener {
            oneAtyHelper?.remove()
        }
        findV<Button>(R.id.tv_input).setOnClickListener {
            oneAtyHelper?.startT<InputCpt>()
        }
        findV<Button>(R.id.tv_dialog).setOnClickListener {
            clickDialog(it.context)
        }
    }

    override fun onRemove() {
        findV<Toolbar>(R.id.t_bar).setNavigationOnClickListener(null)
        super.onRemove()
    }
    private fun clickDialog(ctx: Context) {
        DialogCpt(ctx)
                .title("提示！")
                .content("拉克丝简单快乐就好客来福单快乐就好客来福单快乐就好客来福单快乐就好客来福")
                .tvLeft { d -> d.dismiss() }
                .tvRight { clickDialog(ctx) }
                .show()

    }
}