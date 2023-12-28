package com.mio.music.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.statelayout.StateLayout
import com.lxj.xpopup.core.BottomPopupView
import com.mio.base.dp
import com.mio.base.extension.toast
import com.mio.music.R
import com.mio.music.data.Song
import com.mio.music.databinding.LayoutSongListBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.loadImage
import com.mio.music.manager.MusicPlayer
import com.mio.music.ui.adapter.RvSongListAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class SongListPopupView(
    context: Context,
    private var songs: List<Song>,
    private val iconUrl: String?,
    private val title: String?,
    private val userUrl: String?,
    private val userName: String?,
) :
    BottomPopupView(context) {
    private lateinit var mDataBinding: LayoutSongListBinding

    private val rvAdapter: RvSongListAdapter = RvSongListAdapter()

    private val state: StateLayout by lazy {
        StateLayout(context).wrap(mDataBinding.rvSongList)
    }

    override fun addInnerContent() {
        val contentView =
            LayoutInflater.from(context).inflate(implLayoutId, bottomPopupContainer, false)
        bottomPopupContainer.addView(contentView)
        mDataBinding = DataBindingUtil.bind(contentView)!!
        initView()
        initData()
        popupInfo.apply {
            // 状态栏
            hasStatusBarShadow = false
            hasStatusBar = false
            animationDuration = 400
            enableDrag = true
        }
    }

    private fun initView() {
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
                smartDismiss()
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun initData() {
        postDelayed({
            if (songs.isEmpty()) {
                state.showEmpty()
            } else {
                rvAdapter.setList(songs)
                state.showContent()
            }
        }, 500)
    }

    override fun getPopupWidth(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun getPopupHeight(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_song_list
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            smartDismiss()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}