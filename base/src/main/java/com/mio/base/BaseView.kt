package com.mio.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

/**
 * 绑定一个布局的view
 */
abstract class BaseView<T : ViewDataBinding>(
    context: Context?,
    attrs: AttributeSet?,
    layoutId: Int = 0,
) : FrameLayout(context!!, attrs), LifecycleOwner {
    constructor(context: Context?) : this(context, null)

    var mDataBinding: T

    final override var lifecycle: LifecycleRegistry
    var hasInitView = false


    init {
        mDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), layoutId, this@BaseView, false
        )
        addView(mDataBinding.root)
        lifecycle = LifecycleRegistry(this)

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycle.currentState = Lifecycle.State.RESUMED
        if (!hasInitView) {
            initView()
            hasInitView = true
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycle.currentState = Lifecycle.State.DESTROYED
    }

    abstract fun initView()
}

