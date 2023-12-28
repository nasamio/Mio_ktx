package com.mio.music.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mio.music.R
import com.mio.music.databinding.LayoutRecommendBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.ui.MainFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RvMainContentAdapter :
    BaseMultiItemQuickAdapter<RvMainContentAdapter.RvMainContentItem, BaseViewHolder>() {

    val RECOMMEND_LIST = 1 // 推荐歌单
    val PERSONAL_FM = 2 // 私人FM
    val MUSIC_LIST = 3 // 音乐列表
    val MUSIC_RANK = 4 // 音乐排行榜

    init {
        addItemType(RECOMMEND_LIST, R.layout.layout_recommend)
        addItemType(PERSONAL_FM, R.layout.layout_recommend)
        addItemType(MUSIC_LIST, R.layout.layout_recommend)
        addItemType(MUSIC_RANK, R.layout.layout_recommend)
    }

    override fun convert(holder: BaseViewHolder, item: RvMainContentItem) {
        holder.setText(R.id.tv_title, item.title)
        when (item.itemType) {
            RECOMMEND_LIST -> {
                bindRecommend(LayoutRecommendBinding.bind(holder.itemView), item)
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


    data class RvMainContentItem(
        val title: String,
        override val itemType: Int,
    ) : MultiItemEntity
}