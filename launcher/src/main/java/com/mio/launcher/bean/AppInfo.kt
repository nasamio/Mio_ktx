package com.mio.launcher.bean

import android.graphics.drawable.Drawable

data class AppInfo(
    val name: String,
    val packageName: String,
    val icon: Int,
    val iconDrawable:Drawable?
)
