package com.eericxu.ktcomponets.uicomponet

import android.content.Context
import android.widget.Button
import com.eericxu.baselib.BaseComponent
import com.eericxu.ktcomponets.R

class MainCpt(ctx: Context) : BaseComponent(ctx, R.layout.layout_main) {
    init {
        findV<Button>(R.id.tv_content).apply {
            text = "Next:1"
            setOnClickListener {
                oneAtyHelper.startT<NextCpt>(mutableMapOf("index" to 2))
            }
        }
        findV<Button>(R.id.bt_testTouch).setOnClickListener {
            toast("TestTouch")
        }
        findV<Button>(R.id.tv_input).setOnClickListener {
            oneAtyHelper.startT<InputCpt>()
        }
        findV<Button>(R.id.bt_web).setOnClickListener {
            oneAtyHelper.startT<WebCpt>()
        }
        findV<Button>(R.id.bt_cslayot).setOnClickListener {
            oneAtyHelper.startT<ShareAnimCpt>()
        }
    }
}