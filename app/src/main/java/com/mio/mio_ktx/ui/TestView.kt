package com.mio.mio_ktx.ui

import android.content.Context
import android.util.AttributeSet
import com.mio.base.BaseView
import com.mio.mio_ktx.R
import com.mio.mio_ktx.databinding.ViewTestBinding

class TestView(context: Context?, attrs: AttributeSet?) :
    BaseView<ViewTestBinding>(context, attrs, R.layout.view_test) {
    override fun initView() {
        mDataBinding.tvCenter.text = "我是测试view"
    }


}