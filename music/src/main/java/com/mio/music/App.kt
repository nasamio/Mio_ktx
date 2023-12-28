package com.mio.music

import android.app.Application
import com.mio.music.helper.SpHelper
import com.mio.music.helper.UserHelper
import com.mio.music.manager.MusicPlayer

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SpHelper.init(this)
        UserHelper.init()
        MusicPlayer.init(this)
    }
}