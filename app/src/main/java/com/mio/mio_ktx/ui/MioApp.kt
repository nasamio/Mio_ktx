package com.mio.mio_ktx.ui

import android.app.Application
import cn.jpush.android.api.JPushInterface

class MioApp : Application() {

    override fun onCreate() {
        super.onCreate()

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }
}