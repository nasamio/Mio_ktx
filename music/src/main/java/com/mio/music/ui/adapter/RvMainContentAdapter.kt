package com.mio.music.ui.adapter

import DialogHelper
import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mio.base.Tag.TAG
import com.mio.base.extension.toast
import com.mio.music.R
import com.mio.music.databinding.LayoutIlikeBinding
import com.mio.music.databinding.LayoutRecommendBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.helper.KtorHelper.getILike
import com.mio.music.helper.KtorHelper.getSongDetail
import com.mio.music.helper.UserHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RvMainContentAdapter :
    BaseMultiItemQuickAdapter<RvMainContentAdapter.RvMainContentItem, BaseViewHolder>() {

    val RECOMMEND_LIST = 1 // 推荐歌单
    val I_LIKE = 2 // 我喜欢
    val CREATED_MUSIC_LIST = 3 // 创建的歌单
    val COLLECTED_MUSIC_LIST = 4 // 收藏的歌单

    init {
        addItemType(RECOMMEND_LIST, R.layout.layout_recommend)
        addItemType(I_LIKE, R.layout.layout_ilike)
        addItemType(CREATED_MUSIC_LIST, R.layout.layout_recommend)
        addItemType(COLLECTED_MUSIC_LIST, R.layout.layout_recommend)
    }

    override fun convert(holder: BaseViewHolder, item: RvMainContentItem) {
        holder.setText(R.id.tv_title, item.title)
        when (item.itemType) {
            RECOMMEND_LIST -> {
                bindRecommend(LayoutRecommendBinding.bind(holder.itemView), item)
            }

            I_LIKE -> {
                bindILike(LayoutIlikeBinding.bind(holder.itemView), item)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun bindRecommend(
        binding: LayoutRecommendBinding,
        item: RvMainContentItem,
    ) {
        val rvAdapter = RvRecommendAdapter()
        binding.rvRecommend.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        // 请求数据
        GlobalScope.launch {
            val response = KtorHelper.recommendList()
            if (response.code == 200) {
                val recommendList = response.recommend
                withContext(Dispatchers.Main) {
                    recommendList?.let {
                        // 更新UI
                        rvAdapter.setList(it)
                    }
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    private fun bindILike(binding: LayoutIlikeBinding, item: RvMainContentItem) {
        binding.apply {
            tvTitle.text = "${UserHelper.nickname}喜欢的歌单"

            root.setOnClickListener {
                GlobalScope.launch {
                    UserHelper.uid?.let { it1 ->
                        val response = getILike(it1)
                        if (response.code == 200) {
                            response.ids?.let { ids ->
                                Log.d(TAG, "bindILike: uid:${UserHelper.uid}")
                                Log.d(TAG, "bindILike: ids: $ids")
                                val songListResponse = getSongDetail(ids.toMutableList())
                                if (songListResponse.code == 200) {
                                    songListResponse.songs?.let { songs ->
                                        withContext(Dispatchers.Main) {
                                            songs.forEach {
                                                Log.d(TAG, "bindILike: song: $it")
                                            }
                                            DialogHelper.showMusicList(
                                                context, songs,
                                                UserHelper.backgroundUrl + "?param=100y100",
                                                UserHelper.nickname + "喜欢的歌单",
                                                UserHelper.avatarUrl,
                                                UserHelper.nickname,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    data class RvMainContentItem(
        val title: String,
        override val itemType: Int,
    ) : MultiItemEntity
}