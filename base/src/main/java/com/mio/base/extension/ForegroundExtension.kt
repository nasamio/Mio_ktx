package com.mio.base.extension

import android.annotation.SuppressLint
import android.view.View
import com.mio.base.R

/**
 * 不支持形状填充
 */
@SuppressLint("UseCompatLoadingForDrawables")
fun View.enablePressEffect() {
    foreground = context.getDrawable(R.drawable.fg_ripple_no_corner)
    isClickable = true
    isFocusable = true
}