package com.good.fast.app.tenthwallpaper.ui

import androidx.lifecycle.lifecycleScope
import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.base.BaseActivity
import com.good.fast.app.tenthwallpaper.base.BaseViewModel
import com.good.fast.app.tenthwallpaper.data.TenthDataBean
import com.good.fast.app.tenthwallpaper.data.TenthSqL
import com.good.fast.app.tenthwallpaper.databinding.ActivityBootBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

class BootActivity : BaseActivity<MainViewModel, ActivityBootBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_boot
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        super.initView()
        initSql()
        viewModel.getBlackList(this)
        lifecycleScope.launch {
            delay(2000)
            navigateTo(MainActivity::class.java)
            finish()
        }
    }

    override fun initViewModel() {
        super.initViewModel()
    }

    private fun initSql() {
        val dbHelper = TenthSqL.TaskDbHelper(this)
        val task = TenthDataBean("TenthWall", "", 0, "")
        dbHelper.insertTask(task)
        if(viewModel.getSqHelp(this).tenth_uuid.isEmpty()){
            viewModel.readTask.tenth_uuid = UUID.randomUUID().toString()
            dbHelper.updateTask(viewModel.readTask)
        }
    }
}