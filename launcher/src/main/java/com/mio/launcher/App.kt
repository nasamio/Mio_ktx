package com.mio.launcher

import android.app.Application

class App : Application() {
    companion object {
        val instance by lazy { this }
    }
}