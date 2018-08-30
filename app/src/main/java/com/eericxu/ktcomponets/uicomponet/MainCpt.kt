package com.eericxu.ktcomponets.uicomponet

import android.content.Context
import android.widget.Button
import com.eericxu.baselib.BaseComponent
import com.eericxu.ktcomponets.R

class MainCpt(ctx: Context, index: Int) : BaseComponent(ctx, R.layout.layout_main) {
    init {
        findV<Button>(R.id.tv_content).apply {
            text = "Next:$index"
            setOnClickListener {
                oneAtyHelper.start(NextCpt(ctx, index + 1))
            }
        }
        findV<Button>(R.id.bt_testTouch).setOnClickListener {
            toast("TestTouch")
        }
        findV<Button>(R.id.tv_input).setOnClickListener {
            oneAtyHelper.start(InputCpt(ctx))
        }
    }
}