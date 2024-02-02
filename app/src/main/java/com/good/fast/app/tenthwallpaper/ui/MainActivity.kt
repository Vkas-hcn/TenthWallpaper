package com.good.fast.app.tenthwallpaper.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.base.BaseActivity
import com.good.fast.app.tenthwallpaper.base.BaseViewModel
import com.good.fast.app.tenthwallpaper.data.TenthDataBean
import com.good.fast.app.tenthwallpaper.data.TenthSqL
import com.good.fast.app.tenthwallpaper.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {
    val dbHelper = TenthSqL.TaskDbHelper(this)

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun initView() {
        super.initView()
        initAdapter()
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.imgSetting.setOnClickListener {
            navigateTo(SettingActivity::class.java)
        }
    }
    private fun initAdapter() {
        val adapter = ImageViewAdapter(this)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : ImageViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                navigateTo(DetailActivity::class.java)
                viewModel.readTask.tenth_position = position
                viewModel.readTask.tenth_uuid = viewModel.getSqHelp(this@MainActivity).tenth_uuid
                viewModel.readTask.tenth_clockvalue = viewModel.getSqHelp(this@MainActivity).tenth_clockvalue
                dbHelper.updateTask(viewModel.readTask)
            }
        })
    }
}