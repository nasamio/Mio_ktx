import android.content.Context
import android.content.Intent
import com.lxj.xpopup.XPopup
import com.mio.music.data.Song
import com.mio.music.helper.LiveDataBus
import com.mio.music.ui.activity.SongListActivity
import com.mio.music.ui.view.SongListPopupView

object DialogHelper {
    fun showMusicList(
        context: Context,
        songs: List<Song>,
        iconUrl: String?,
        title: String?,
        userUrl: String?,
        userName: String?,
    ) {
        LiveDataBus.get().with("songs").postValue(songs)
        LiveDataBus.get().with("iconUrl").postValue(iconUrl)
        LiveDataBus.get().with("title").postValue(title)
        LiveDataBus.get().with("userUrl").postValue(userUrl)
        LiveDataBus.get().with("userName").postValue(userName)
        // 跳转到SongListActivity
        context.startActivity(Intent(context, SongListActivity::class.java))
    }

    fun showMiniDialog(context: Context) {
        MiniDialog(context).show()
    }

}
