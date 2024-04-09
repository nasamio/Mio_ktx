package com.mio.base.utils

import android.annotation.SuppressLint
import com.bumptech.glide.request.RequestOptions
import com.mio.base.R

object ImageLoader {
    var placeholderRes = R.drawable.placeholder
    var errorRes = R.drawable.error

    /**
     * 全局配置对应的加载配置
     */
    fun config(
        placeholderRes: Int = R.drawable.placeholder,
        errorRes: Int = R.drawable.error,
    ) {
        this.errorRes = errorRes
        this.placeholderRes = placeholderRes
    }

    @SuppressLint("CheckResult")
    val option = RequestOptions().apply {
        placeholder(placeholderRes)
        error(errorRes)
    }
}