package com.mio.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mio.base.databinding.LayoutRootBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewDataBinding>(
    private val layoutId: Int,
//    private val loadingLayoutId: Int = R.layout.layout_loading,
//    private val errorLayoutId: Int = R.layout.layout_error
) : Fragment() {
    lateinit var mDataBinding: T
    lateinit var mRootBinding: LayoutRootBinding
    var showInitTag: Boolean = false
    val mContext: Context by lazy { requireContext() }
    lateinit var last: BaseFragment<*> //存储上一个fragment 用以返回键使用
    private var state: UiState = UiState.Content
    lateinit var loadingView: View
    lateinit var errorView: View
    var needAnimatorEveryTime: Boolean = true // 是否每次返回都执行layout animator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        mRootBinding = DataBindingUtil.inflate(inflater, R.layout.layout_root, container, false)
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        mRootBinding.flContainer.addView(mDataBinding.root)
        loadingView = onCreateLoadingView()
        errorView = onCreateErrorView()

        initView()
//        if (showInitTag) Log.d(Tag.TAG, "init view...")
        initObserver()
//        if (showInitTag) Log.d(Tag.TAG, "init observer...")
        initData()
//        if (showInitTag) Log.d(Tag.TAG, "init data...")
        return mRootBinding.root
    }

    /**
     * 如果要自定义view来修改加载效果 重写该方法
     */
    open fun onCreateLoadingView(): View {
        return DataBindingUtil.inflate<ViewDataBinding?>(
            LayoutInflater.from(mContext),
            getLoadingLayoutId(),
            mRootBinding.flBefore,
            false
        ).root
    }

    /**
     * 如果要自定义view来修改错误效果 重写该方法
     */
    open fun onCreateErrorView(): View {
        return DataBindingUtil.inflate<ViewDataBinding?>(
            LayoutInflater.from(mContext),
            getErrorLayoutId(),
            mRootBinding.flBefore,
            false
        ).root
    }

    /**
     * 如果要修改错误的布局 重写该方法
     */
    open fun getErrorLayoutId(): Int {
        return R.layout.layout_error
    }

    /**
     * 如果要修改加载的布局 重写该方法
     */
    open fun getLoadingLayoutId(): Int {
        return R.layout.layout_loading
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden && needAnimatorEveryTime) {
            (mDataBinding.root as ViewGroup).let {
                it.defaultAnimation()
                it.layoutAnimation.start()
            }
        }
    }

    open fun showError() {
        onError()

        state = UiState.Error
    }

    open fun showContent() {
        onContent()

        state = UiState.Content
    }

    open fun onContent() {
        mRootBinding.flBefore.removeAllViews()
    }

    open fun onError() {
        mRootBinding.flBefore.removeAllViews()

        mRootBinding.flBefore.addView(errorView)
    }

    open fun showLoading() {
        onLoading()

        state = UiState.Loading
    }

    /**
     * 显示加载的时候触发 可以在这里自定义加载效果
     */
    open fun onLoading() {
        mRootBinding.flBefore.removeAllViews()
        mRootBinding.flBefore.addView(loadingView)

        lifecycleScope.launch(Dispatchers.Main) {
            loadingView.findViewById<ProgressBar>(R.id.progress)
                ?.let {
                    while (state == UiState.Loading) {
                        it.background = null // 这里不知道为什么xml设置不行
                        it.progress = it.progress + 2
                        delay(100)
                    }
                }
        }
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
     * 处理按键事件
     */
    fun handleKey(keyCode: Int): Boolean {
        return false
    }
}