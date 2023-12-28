package com.mio.music.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mio.base.BaseFragment
import com.mio.music.R
import com.mio.music.databinding.FragmentMainBinding
import com.mio.music.ui.adapter.RvMainContentAdapter
import com.mio.music.ui.adapter.RvMainMenuAdapter

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val rvMainMenuAdapter: RvMainMenuAdapter by lazy {
        RvMainMenuAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> {
                        //每日推荐
                    }

                    1 -> {
                        //私人漫游
                    }

                    2 -> {
                        //歌单
                    }

                    3 -> {
                        //排行榜
                    }
                }
            }
            setList(
                mutableListOf(
                    RvMainMenuAdapter.RvMainMenuItem("每日推荐", R.drawable.recommend),
                    RvMainMenuAdapter.RvMainMenuItem("私人漫游", R.drawable.personal_roam),
                    RvMainMenuAdapter.RvMainMenuItem("歌单", R.drawable.song_list),
                    RvMainMenuAdapter.RvMainMenuItem("排行榜", R.drawable.ranking),
                )
            )
        }
    }

    private val rvMainContentAdapter: RvMainContentAdapter by lazy {
        RvMainContentAdapter().apply {
            setNewData(
                mutableListOf(
                    RvMainContentAdapter.RvMainContentItem("推荐歌单", RECOMMEND_LIST),
                    RvMainContentAdapter.RvMainContentItem("私人FM", PERSONAL_FM),
                    RvMainContentAdapter.RvMainContentItem("音乐列表", MUSIC_LIST),
                    RvMainContentAdapter.RvMainContentItem("音乐排行榜", MUSIC_RANK),
                )
            )
        }
    }


    override fun initView() {
        initRvMain()
        initRvContent()
    }

    override fun initObserver() {
    }

    override fun initData() {

    }

    private fun initRvMain() {
        mDataBinding.rvMainMenu.apply {
            adapter = rvMainMenuAdapter
            layoutManager = GridLayoutManager(context, 4)
        }
    }

    private fun initRvContent() {
        mDataBinding.rvMainContent.apply {
            adapter = rvMainContentAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


}