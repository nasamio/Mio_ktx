package com.mio.music.ui.view

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.lxj.xpopup.core.CenterPopupView
import com.mio.music.R
import com.mio.music.databinding.LayoutLoginBinding

class LoginView(context: Context) : CenterPopupView(context) {
    lateinit var mDataBinding: LayoutLoginBinding
    override fun addInnerContent() {
        super.addInnerContent()
        mDataBinding = DataBindingUtil.bind(contentView)!!
        popupInfo.apply {
            isDismissOnTouchOutside = false
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_login
    }

}