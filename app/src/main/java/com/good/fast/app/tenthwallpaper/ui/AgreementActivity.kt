package com.good.fast.app.tenthwallpaper.ui

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.base.BaseActivity
import com.good.fast.app.tenthwallpaper.databinding.ActivityAgreementBinding
import com.good.fast.app.tenthwallpaper.databinding.ActivitySettingBinding

class AgreementActivity : BaseActivity<MainViewModel, ActivityAgreementBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_agreement
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        super.initView()
        binding.imageViewBack.setOnClickListener {
            navigateTo(BootActivity::class.java)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.webviewAgreement.apply {
            loadUrl("https://www.baidu.com/")
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return true
                }
            }
        }
    }
}