package com.mio.account.view

import android.content.Context
import com.mio.account.R
import com.mio.account.databinding.LayoutDialogLoginBinding
import com.mio.base.extension.addAfterTextChangeListener
import com.mio.base.setClickListener

class LoginXpop(
    context: Context,
    var listener: (String, String) -> Unit,
    val title: String = "登录",
    val content: String = "请输入账号密码",
    val cancel: String = "取消",
    val confirm: String = "确定",
) : OwnBaseXpop<LayoutDialogLoginBinding>(context, R.layout.layout_dialog_login) {

    override fun initView() {
        mDataBinding.apply {
            tvTitle.text = title
            tvContent.text = content
            tvCancel.text = cancel
            tvConfirm.text = confirm
            // 监听输入框变化

            var name: String = ""
            var password: String = ""
            tvConfirm.isEnabled = false
            etName.addAfterTextChangeListener {
                name = it

                tvConfirm.isEnabled = name.isNotEmpty() && password.isNotEmpty()
            }
            etPwd.addAfterTextChangeListener {
                password = it

                tvConfirm.isEnabled = name.isNotEmpty() && password.isNotEmpty()
            }

            tvCancel.setClickListener(1000) {
                smartDismiss()
            }
            tvConfirm.setClickListener(1000) {
                listener(name, password)
                smartDismiss()
            }
        }
    }

}