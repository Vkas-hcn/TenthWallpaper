package com.good.fast.app.tenthwallpaper.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<VM : ViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: DB
    protected lateinit var viewModel: VM
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        initViewModel()
        initView()
    }

    abstract fun getLayoutRes(): Int

    abstract fun getViewModelClass(): Class<VM>

    open fun initViewModel() {
        // 可以在子类中初始化 ViewModel
    }

    open fun initView() {
        // 可以在子类中初始化视图
    }

    // 跳转到下一个 Activity
    protected fun navigateTo(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    // 带参数跳转到下一个 Activity
    protected fun navigateToWithArgs(clazz: Class<*>, args: Bundle) {
        val intent = Intent(this, clazz)
        intent.putExtras(args)
        startActivity(intent)
    }

    // 显示自定义弹框
    protected fun showCustomDialog() {
        // 实现自定义弹框的逻辑
    }

    // 显示进度对话框
    protected fun showProgressDialog(message: String) {
//        progressDialog = ProgressDialogUtil.showProgressDialog(this, message)
    }

    // 隐藏进度对话框
    protected fun hideProgressDialog() {
        progressDialog?.dismiss()
    }
}
