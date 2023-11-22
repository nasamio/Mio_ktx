package com.mio.account

import android.app.Application
import com.mio.account.net.ErrorHelper

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        App.init(this)
        ErrorHelper.init(this)
    }
}