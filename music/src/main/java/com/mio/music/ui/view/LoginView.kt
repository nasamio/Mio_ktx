package com.mio.music.ui.view

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.lxj.xpopup.core.CenterPopupView
import com.mio.music.R
import com.mio.music.databinding.LayoutLoginBinding

class LoginView(context: Context) : CenterPopupView(context) {
    val mDataBinding: LayoutLoginBinding by lazy {
        contentView =
            LayoutInflater.from(context).inflate(implLayoutId, centerPopupContainer, false)
        DataBindingUtil.bind(contentView)!!
    }

    override fun addInnerContent() {
        mDataBinding.etCode.inputType = InputType.TYPE_CLASS_NUMBER
        val params = contentView.layoutParams as LayoutParams
        params.gravity = Gravity.CENTER
        centerPopupContainer.addView(contentView, params)
        popupInfo.apply {
            isDismissOnTouchOutside = false
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_login
    }

}