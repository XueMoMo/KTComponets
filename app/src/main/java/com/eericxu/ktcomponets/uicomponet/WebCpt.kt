package com.eericxu.ktcomponets.uicomponet

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.Toolbar
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.eericxu.baselib.BaseComponent
import com.eericxu.ktcomponets.R

class WebCpt(ctx: Context) : BaseComponent(ctx, R.layout.layout_web) {
    init {
        findV<Toolbar>(R.id.t_bar).apply {
            setPadding(0, statusHeight, 0, 0)
            setNavigationOnClickListener {
                oneAtyHelper?.remove()
            }
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onStarted() {
        view.post {
            findV<WebView>(R.id.web).apply {
                settings.domStorageEnabled = true
                settings.defaultTextEncodingName = "UTF-8"
                settings.setSupportZoom(false)
                settings.displayZoomControls = false
                settings.javaScriptEnabled = true
                settings.blockNetworkImage = true
                settings.cacheMode = WebSettings.LOAD_NO_CACHE
                settings.setAppCacheEnabled(false)
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
                loadUrl("https://www.baidu.com")
            }

        }
    }

}