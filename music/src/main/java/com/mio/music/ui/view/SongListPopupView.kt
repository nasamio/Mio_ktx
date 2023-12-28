package com.mio.music.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.statelayout.StateLayout
import com.lxj.xpopup.core.BottomPopupView
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.extension.toast
import com.mio.music.R
import com.mio.music.data.RecommendItem
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
class SongListPopupView(context: Context, private var recommendItem: RecommendItem) :
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
            ivImage.loadImage(recommendItem.picUrl, 5.dp)
            tvTitle.text = recommendItem.name
            ivAuthor.loadImage(recommendItem.creator.avatarUrl, 20.dp)
            tvAuthor.text = recommendItem.creator.nickname
            tvTitleTop.text = recommendItem.name

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

        GlobalScope.launch {
            val songListResponse = KtorHelper.getSongList(recommendItem.id)
            withContext(Dispatchers.Main) {
                if (songListResponse.code == 200 && (songListResponse.songs?.isNotEmpty() == true)) {
                    state.showContent()
                    rvAdapter.setList(songListResponse.songs.toMutableList())
                } else {
                    state.showError()
                    context.toast("获取歌单失败")
                }
            }
        }
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