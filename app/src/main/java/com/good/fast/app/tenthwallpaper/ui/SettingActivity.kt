package com.good.fast.app.tenthwallpaper.ui

import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.base.BaseActivity
import com.good.fast.app.tenthwallpaper.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<MainViewModel, ActivitySettingBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_setting
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        super.initView()
        binding.imageViewBack.setOnClickListener {
            finish()
        }

        binding.textviewSetting.setOnClickListener {
            navigateTo(AgreementActivity::class.java)
        }
    }

    override fun initViewModel() {
        super.initViewModel()
    }
}