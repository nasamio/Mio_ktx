package com.mio.music.ui.activity

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.statelayout.StateLayout
import com.mio.base.BaseActivity
import com.mio.base.dp
import com.mio.music.R
import com.mio.music.data.Song
import com.mio.music.databinding.LayoutSongListBinding
import com.mio.music.helper.LiveDataBus
import com.mio.music.loadImage
import com.mio.music.manager.MusicPlayer
import com.mio.music.ui.adapter.RvSongListAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class SongListActivity : BaseActivity<LayoutSongListBinding>(R.layout.layout_song_list) {
    private val rvAdapter: RvSongListAdapter = RvSongListAdapter()

    private val state: StateLayout by lazy {
        StateLayout(this).wrap(mDataBinding.rvSongList)
    }
    private var songs: MutableList<Song> = mutableListOf()
    private var iconUrl: String? = null
    private var title: String? = null
    private var userUrl: String? = null
    private var userName: String? = null
    override fun initView() {
        songs =
            (LiveDataBus.get().with("songs", MutableList::class.java).value as MutableList<Song>?)!!
        iconUrl = LiveDataBus.get().with("iconUrl", String::class.java).value
        title = LiveDataBus.get().with("title", String::class.java).value
        userUrl = LiveDataBus.get().with("userUrl", String::class.java).value
        userName = LiveDataBus.get().with("userName", String::class.java).value

        mDataBinding.rvSongList.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        mDataBinding.coor.viewTreeObserver.addOnPreDrawListener {
            if (mDataBinding.clTop.height != 0 && mDataBinding.llTop.height != 0) {
                val currentOffset = abs(mDataBinding.appBar.top)
                val alpha =
                    currentOffset * 1f / (mDataBinding.clTop.height - mDataBinding.llTop.height)
                mDataBinding.tvTitleTop.alpha = alpha
                mDataBinding.tvTc.alpha = 1 - alpha
            }
            true
        }

        mDataBinding.apply {
            ivImage.loadImage(iconUrl, 5.dp)
            tvTitle.text = title
            ivAuthor.loadImage(userUrl, 20.dp)
            tvAuthor.text = userName
            tvTitleTop.text = title
            ivBack.setOnClickListener {
                finish()
            }

            ivPlayAll.setOnClickListener {
                MusicPlayer.playMusicList(rvAdapter.data)
            }
        }


        // 设置cl top的高度是屏幕宽度的80%
//        val layoutParams = mDataBinding.clTop.layoutParams
//        layoutParams.height = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
//        mDataBinding.clTop.layoutParams = layoutParams

        // 加载顶部背景图
//        mDataBinding.clTop.loadBg(recommendItem.picUrl)
//        mDataBinding.ivBack.loadImage(recommendItem.picUrl)

        state.showLoading()
    }

    override fun initObserver() {
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun initData() {
        lifecycleScope.launch {
            delay(500)
            if (songs.isEmpty()) {
                state.showEmpty()
            } else {
                rvAdapter.setList(songs)
                state.showContent()
            }
        }
    }

}