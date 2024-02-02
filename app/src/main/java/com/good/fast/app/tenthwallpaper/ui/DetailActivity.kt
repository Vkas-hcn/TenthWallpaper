package com.good.fast.app.tenthwallpaper.ui

import android.app.WallpaperManager
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.base.BaseActivity
import com.good.fast.app.tenthwallpaper.base.BaseViewModel
import com.good.fast.app.tenthwallpaper.data.DataUtils
import com.good.fast.app.tenthwallpaper.data.TenthSqL
import com.good.fast.app.tenthwallpaper.databinding.ActivityBootBinding
import com.good.fast.app.tenthwallpaper.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailActivity : BaseActivity<MainViewModel, ActivityDetailBinding>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_detail
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }
    private var wallData = 0

    override fun initView() {
        super.initView()
        wallData = viewModel.getSqHelp(this).tenth_position
        binding.imageviewDetail.setImageResource(DataUtils.localImageData[wallData])
    }

    override fun initViewModel() {
        super.initViewModel()
        clickFun()
    }

    private fun clickFun() {
        binding.imageviewBack.setOnClickListener {
           finish()
        }
        binding.textviewApply.setOnClickListener {
            showSetDialog()
        }
        binding.tvCancel.setOnClickListener {
            dismissSetDialog()
        }
        binding.tvHS.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataUtils.setWallFun(this@DetailActivity,DataUtils.localImageData[wallData], WallpaperManager.FLAG_SYSTEM, loadBefore = {
                    showSetLoading()
                }, loadAfter = {
                    Toast.makeText(this@DetailActivity, "Set Home Screen Success", Toast.LENGTH_SHORT).show()
                    dismissSetDialog()
                    dismissSetLoading()
                })
            }
        }
        binding.tvLS.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataUtils.setWallFun(this@DetailActivity,DataUtils.localImageData[wallData], WallpaperManager.FLAG_LOCK, loadBefore = {
                    showSetLoading()
                }, loadAfter = {
                    Toast.makeText(this@DetailActivity, "Set Lock Screen Success", Toast.LENGTH_SHORT).show()
                    dismissSetDialog()
                    dismissSetLoading()
                })
            }
        }
        binding.tvB.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataUtils.setWallFun(this@DetailActivity,DataUtils.localImageData[wallData], WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM, loadBefore = {
                    showSetLoading()
                }, loadAfter = {
                    Toast.makeText(this@DetailActivity, "Set Both Screen Success", Toast.LENGTH_SHORT).show()
                    dismissSetDialog()
                    dismissSetLoading()
                })
            }
        }
        binding.textviewDownload.setOnClickListener {
            viewModel.saveDrawableToGallery(this,DataUtils.localImageData[wallData],"images","images",showDialog = {
                showPermissionExplanationDialog(this)
            })
        }
    }

    private fun showSetDialog() {
        binding.clDialog.visibility = View.VISIBLE
    }
    private fun dismissSetDialog() {
        binding.clDialog.visibility = View.GONE
    }
    private fun showSetLoading() {
        binding.progressSet.visibility = View.VISIBLE
    }
    private fun dismissSetLoading() {
        binding.progressSet.visibility = View.GONE
    }
    private fun showPermissionExplanationDialog(activity: AppCompatActivity) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Permission requests")
        builder.setMessage("This app needs permission to save images to your device. You can grant them in app settings.")
        builder.setPositiveButton("Go to Settings") { _: DialogInterface, _: Int ->
            val intent = android.content.Intent().apply {
                action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = android.net.Uri.fromParts("package", activity.packageName, null)
            }
            activity.startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2000 -> {
                viewModel.saveDrawableToGallery(this,DataUtils.localImageData[wallData],"images","images",showDialog = {
                    showPermissionExplanationDialog(this)
                })            }
        }
    }
}