package com.mio.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.mio.base.Tag.TAG
import com.mio.base.databinding.LayoutRootBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 不带有view model的activity基类
 */
abstract class BaseActivity<T : ViewDataBinding>(private val layoutId: Int) :
    AppCompatActivity() {
    lateinit var mDataBinding: T
    lateinit var mRootBinding: LayoutRootBinding
    var showInitTag: Boolean = false
    var hideActionBar = true
    private var state: UiState = UiState.Content
    lateinit var loadingView: View
    lateinit var errorView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hideActionBar) supportActionBar?.hide()

        mRootBinding = DataBindingUtil
            .setContentView(this@BaseActivity, R.layout.layout_root)

        mDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this@BaseActivity),
            layoutId,
            mRootBinding.flContainer,
            false
        )
        mRootBinding.flContainer.addView(mDataBinding.root)
        loadingView = onCreateLoadingView()
        errorView = onCreateErrorView()

        initView()
//        if (showInitTag) Log.d(TAG, "init view...")
        initObserver()
//        if (showInitTag) Log.d(TAG, "init observer...")
        initData()
//        if (showInitTag) Log.d(TAG, "init data...")
    }

    /**
     * 如果要自定义view来修改加载效果 重写该方法
     */
    open fun onCreateLoadingView(): View {
        return DataBindingUtil.inflate<ViewDataBinding?>(
            LayoutInflater.from(this@BaseActivity),
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
            LayoutInflater.from(this@BaseActivity),
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
     * 设置全局 日志tag
     */
    fun setGlobalTag(tag: String) {
        TAG = tag;
    }
}