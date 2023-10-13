package com.mio.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mio.base.Tag.TAG

/**
 * 不带有view model的activity基类
 */
abstract class BaseActivity<T : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {
    lateinit var mDataBinding: T
    var showInitTag: Boolean = false
    var hideActionBar = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hideActionBar) supportActionBar?.hide()

        mDataBinding = DataBindingUtil.setContentView(this@BaseActivity, layoutId)

        initView()
        if (showInitTag) Log.d(TAG, "init view...")
        initObserver()
        if (showInitTag) Log.d(TAG, "init observer...")
        initData()
        if (showInitTag) Log.d(TAG, "init data...")
    }

    /**
     * 初始化界面组件
     */
    abstract fun initView()

    /**
     * 初始化观察者
     */
    abstract fun initObserver()

    /**
     * 初始化数据获取
     */
    abstract fun initData()

    /**
     * 设置全局 日志tag
     */
    fun setGlobalTag(tag: String) {
        TAG = tag;
    }
}