package com.mio.launcher_ktx.bean

import android.graphics.drawable.Drawable

data class AppInfo(
    val pkgName: String,
    val appName: String,
    val appIconDrawable: Drawable?
)
