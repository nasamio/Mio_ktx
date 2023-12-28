import android.content.Context
import com.lxj.xpopup.XPopup
import com.mio.music.data.Song
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
        XPopup.Builder(context)
            .asCustom(SongListPopupView(context, songs, iconUrl, title, userUrl, userName))
            .show()
    }

}
