package com.mio.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : ViewDataBinding>(private val layoutId: Int) : Fragment() {
    lateinit var mDataBinding: T
    var showInitTag: Boolean = false
    val mContext: Context by lazy { requireContext() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView()
        if (showInitTag) Log.d(Tag.TAG, "init view...")
        initObserver()
        if (showInitTag) Log.d(Tag.TAG, "init observer...")
        initData()
        if (showInitTag) Log.d(Tag.TAG, "init data...")
        return mDataBinding.root
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
}