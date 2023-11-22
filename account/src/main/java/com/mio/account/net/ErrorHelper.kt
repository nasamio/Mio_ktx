package com.mio.account.net

import android.annotation.SuppressLint
import android.content.Context
import com.mio.base.extension.longToast

@SuppressLint("StaticFieldLeak")
object ErrorHelper {
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context
    }

    fun handleError(code: Int) {
        when (code) {
            Code.USER_NOT_EXISTS -> context.longToast("用户不存在")
            Code.USER_EXISTS -> context.longToast("用户已存在")
            Code.USER_PASSWORD_ERROR -> context.longToast("密码错误")
            else -> context.longToast("未知错误：$code")
        }
    }
}