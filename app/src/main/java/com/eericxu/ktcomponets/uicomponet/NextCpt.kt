package com.eericxu.ktcomponets.uicomponet

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.Toolbar
import android.widget.Button
import com.eericxu.baselib.BaseComponent
import com.eericxu.baselib.ui.DialogCpt
import com.eericxu.ktcomponets.R


class NextCpt(ctx: Context, datas: MutableMap<String, Any>) : BaseComponent(ctx, R.layout.layout_next) {
    init {
        val r = (Math.random() * 255).toInt()
        val g = (Math.random() * 255).toInt()
        val b = (Math.random() * 255).toInt()
        val index = datas["index"] as Int
        findV<Button>(R.id.tv_content).apply {
            text = "Next:$index"
            setOnClickListener {
                oneAtyHelper.startT<NextCpt>(mutableMapOf("index" to index + 1))
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
            oneAtyHelper.startT<InputCpt>()
        }
        findV<Button>(R.id.tv_dialog).setOnClickListener {
            clickDialog(ctx)
        }
    }

    fun clickDialog(ctx: Context) {
        DialogCpt(ctx)
                .title("提示！")
                .content("拉克丝简单快乐就好客来福单快乐就好客来福单快乐就好客来福单快乐就好客来福")
                .tvLeft { d -> d.dismiss() }
                .tvRight { clickDialog(ctx) }
                .show()

    }
}