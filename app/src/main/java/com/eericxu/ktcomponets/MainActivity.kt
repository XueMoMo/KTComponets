package com.eericxu.ktcomponets

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import com.eericxu.baselib.OneAty
import com.eericxu.baselib.manager.OneAtyHelper
import com.eericxu.ktcomponets.uicomponet.MainCpt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OneAty {
    override fun getRoot(): ViewGroup {
        return root
    }

    val mHelper by lazy { OneAtyHelper(this) }

    override fun getHelper(): OneAtyHelper {
        return mHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show()
        mHelper.onCreateAty(savedInstanceState)
        mHelper.start(MainCpt(this, 1), null)
    }

    override fun onStart() {
        super.onStart()
        mHelper.onStart()
    }

    override fun onResume() {
        super.onResume()
        mHelper.onResume()
    }

    override fun onPause() {
        super.onPause()
        mHelper.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        mHelper.onRestart()
    }


    override fun onStop() {
        super.onStop()
        mHelper.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHelper.onDestroy()
    }

    override fun onBackPressed() {
        if (!mHelper.back()) {
            super.onBackPressed()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mHelper.onConfigurationChanged(newConfig)
    }
}
