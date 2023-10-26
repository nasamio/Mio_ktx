import android.media.MediaPlayer
import android.os.Handler
import com.mio.music.data.Music
import com.mio.music.formatToTwoDecimalPlaces

object MusicManager {

    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }
    var currentMusicIndex: Int = -1
    private val musicList: MutableList<Music> by lazy { mutableListOf() }
    private var progressCallback: ((Float) -> Unit)? = null
    private var playStatusCallback: ((Boolean) -> Unit)? = null
    private val handler = Handler()

    // 初始化音乐列表
    fun initialize() {
        mediaPlayer.setOnPreparedListener {
            // 定期获取进度并触发回调
            handler.post(object : Runnable {
                override fun run() {
                    val progress =
                        (mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration.toFloat() * 100f)
                            .formatToTwoDecimalPlaces()
                    progressCallback?.invoke(progress)

                    // 每100毫秒回调一次
                    handler.postDelayed(this, 100)
                }
            })
        }
    }

    fun addMusic(list: MutableList<Music>) {
        musicList.addAll(list)
    }

    // 设置进度回调
    fun setProgressCallback(callback: (Float) -> Unit) {
        progressCallback = callback
    }

    // 设置状态回调
    fun setPlayingStatusCallback(callback: (Boolean) -> Unit) {
        playStatusCallback = callback
    }

    // 播放音乐
    fun playMusic(index: Int) {
        if (index < 0 || index >= musicList.size) {
            return
        }

        currentMusicIndex = index

        mediaPlayer.apply {
            reset()
            setDataSource(musicList[index].path)
            prepare()
            start()
        }

        playStatusCallback?.invoke(true)
    }

    // 下一首 先默认为循环模式
    fun playNext() {
        playMusic(if (currentMusicIndex + 1 > musicList.size - 1) 0 else currentMusicIndex + 1)
    }

    // 上一首 先默认为循环模式
    fun playPrevious() {
        playMusic(if (currentMusicIndex - 1 >= 0) currentMusicIndex - 1 else musicList.size - 1)
    }

    // 暂停音乐
    fun pauseMusic() {
        mediaPlayer.pause()

        playStatusCallback?.invoke(false)
    }

    // 继续播放音乐
    fun resumeMusic() {
        mediaPlayer.start()

        playStatusCallback?.invoke(true)
    }

    // 停止音乐
    fun stopMusic() {
        mediaPlayer.stop()
        mediaPlayer.reset()

        playStatusCallback?.invoke(false)
    }

    // 释放 MediaPlayer 资源
    fun releaseMediaPlayer() {
        handler.removeCallbacksAndMessages(null) // 停止回调
        mediaPlayer.release()
    }

    // 获取当前播放的音乐
    fun getCurrentMusic(): Music? {
        if (currentMusicIndex >= 0 && currentMusicIndex < musicList.size) {
            return musicList[currentMusicIndex]
        }
        return null
    }

    // 是否在播放
    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}
