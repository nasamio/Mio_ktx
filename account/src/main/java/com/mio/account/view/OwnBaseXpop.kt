package com.mio.account.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lxj.xpopup.core.CenterPopupView

abstract class OwnBaseXpop<T : ViewDataBinding>(context: Context, val layoutId: Int = 0) :
    CenterPopupView(context) {
    lateinit var mDataBinding: T
    override fun addInnerContent() {
        mDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context), layoutId, centerPopupContainer, false
        )
        contentView = mDataBinding.root
        val params = contentView.layoutParams as LayoutParams
        params.gravity = Gravity.CENTER
        centerPopupContainer.addView(contentView, params)
        initView()
    }

    abstract fun initView()

    override fun getImplLayoutId(): Int {
        return 0
    }
}