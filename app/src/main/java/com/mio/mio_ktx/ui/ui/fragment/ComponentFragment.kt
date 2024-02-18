package com.mio.mio_ktx.ui.ui.fragment

import com.mio.base.BaseFragment
import com.mio.base.extension.bg
import com.mio.base.extension.enablePressEffect
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.FragmentComponentBinding

class ComponentFragment : BaseFragment<FragmentComponentBinding>(R.layout.fragment_component) {
    override fun initView() {
        testBgDrawable()
    }

    private fun testBgDrawable() {
        mDataBinding.tv1.apply {
            // 背景圆角
            bg(mContext.getColor(com.mio.base.R.color.black_30), 5f, 5)
            // 前景按压
            enablePressEffect()
        }
        mDataBinding.btn1.apply {
            // 背景圆角
            bg(mContext.getColor(com.mio.base.R.color.black_30), 20f, 5)
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }
}