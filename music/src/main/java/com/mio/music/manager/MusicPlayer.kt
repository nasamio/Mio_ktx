package com.mio.music.manager

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import com.mio.base.Tag.TAG
import com.mio.base.extension.toast
import com.mio.music.data.Song
import com.mio.music.helper.KtorHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("StaticFieldLeak")
object MusicPlayer {
    private var mMediaPlayer: MediaPlayer? = null
    private val handler = android.os.Handler()
    private const val PROGRESS_UPDATE_INTERVAL = 100L

    const val PLAY_STATE_NONE = 0
    const val PLAY_STATE_LOADING = 1
    const val PLAY_STATE_PLAYING = 2

    var playList: MutableList<Song> = mutableListOf() // 播放列表
    var currentIndex: ObservableInt = ObservableInt(-1) // 当前播放的歌曲索引
    var playState: ObservableInt = ObservableInt(PLAY_STATE_NONE) // 0: 未播放 1:加载 2:播放

    var playMode: ObservableInt = ObservableInt(0) // 0: 顺序播放 1: 随机播放 2: 单曲循环
    var progress: ObservableFloat = ObservableFloat(0f) // 播放进度

    lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.apply {
            setOnCompletionListener {
                when (playMode.get()) {
                    0 -> {
                        if (currentIndex.get() == playList.size - 1) {
                            playMusic(0)
                        } else {
                            playMusic(currentIndex.get() + 1)
                        }
                    }

                    1 -> {
                        playMusic((0 until playList.size).random())
                    }

                    2 -> {
                        playMusic(currentIndex.get())
                    }
                }
            }
        }
        // 循环发送进度
        handler.post(object : Runnable {
            override fun run() {
                mMediaPlayer?.let {
                    if (it.isPlaying) {
                        progress.set((it.currentPosition.toFloat() / it.duration * 100))
                    }
                }
                handler.postDelayed(this, PROGRESS_UPDATE_INTERVAL)
            }
        })
    }

    fun playMusicList(songs: MutableList<Song>) {
        playState.set(PLAY_STATE_LOADING)
        playList.clear()
        playList.addAll(songs)
        currentIndex.set(0)
        playMusic(0)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun playMusic(index: Int) {
        if (index < 0 || index >= playList.size) {
            return
        }
        GlobalScope.launch {
            val urlResponse = KtorHelper.getSongUrl(playList[index].id)
            if (urlResponse.code == 200) {
                currentIndex.set(index)

                Log.d(TAG, "playMusic: data:${urlResponse.data}")

                urlResponse.data?.get(0)?.url?.let {
                    mMediaPlayer?.apply {
                        try {
                            reset()
                            setDataSource(it)
                            prepare()
                            start()
                            playState.set(PLAY_STATE_PLAYING)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            playState.set(PLAY_STATE_NONE)
                            withContext(Dispatchers.Main) {
                                mContext.toast("播放出错!")
                            }
                        }
                        Log.d(TAG, "playMusic: 开始播放")
                    }
                }
            }
        }


    }

    fun playMusic(song: Song) {
        val index = playList.indexOf(song)
        if (index < 0) {
            playMusicList(mutableListOf(song))
        }
        playMusic(index)
    }

    fun currentMusic(): Song? {
        if (currentIndex.get() < 0 || currentIndex.get() >= playList.size) {
            return null
        }
        return playList[currentIndex.get()]
    }

    fun pause() {
        mMediaPlayer?.apply {
            if (isPlaying) {
                pause()
                playState.set(PLAY_STATE_NONE)
            }
        }
    }

    fun play() {
        mMediaPlayer?.apply {
            if (!isPlaying) {
                start()
                playState.set(PLAY_STATE_PLAYING)
            }
        }
    }
}