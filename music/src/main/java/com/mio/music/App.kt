package com.mio.music

import android.app.Application
import androidx.lifecycle.lifecycleScope
import com.mio.base.addChangeCallback
import com.mio.music.helper.SpHelper
import com.mio.music.helper.UserHelper
import com.mio.music.manager.MusicPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App : Application() {
    companion object status {
        var shouMiniOffsetY = true
    }

    override fun onCreate() {
        super.onCreate()
        SpHelper.init(this)
        UserHelper.init()
        MusicPlayer.init(this)

        observerMini()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun observerMini() {
        MusicPlayer.playState.addChangeCallback {
            when (it) {
                MusicPlayer.PLAY_STATE_NONE -> {
                }

                MusicPlayer.PLAY_STATE_LOADING -> {
                }

                MusicPlayer.PLAY_STATE_PLAYING -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        DialogHelper.showMiniDialog(this@App)
                    }
                }
            }
        }
    }


}