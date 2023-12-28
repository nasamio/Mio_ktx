package com.mio.music.ui.adapter

import DialogHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mio.base.dp
import com.mio.base.extension.toast
import com.mio.base.setClickListener
import com.mio.music.R
import com.mio.music.data.RecommendItem
import com.mio.music.databinding.ItemRecommendBinding
import com.mio.music.helper.KtorHelper
import com.mio.music.loadImage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RvRecommendAdapter :
    BaseQuickAdapter<RecommendItem, BaseDataBindingHolder<ItemRecommendBinding>>(R.layout.item_recommend) {
    @OptIn(DelicateCoroutinesApi::class)
    override fun convert(holder: BaseDataBindingHolder<ItemRecommendBinding>, item: RecommendItem) {
        holder.dataBinding?.apply {
            ivBg.loadImage(item.picUrl, 10.dp)
            tvName.text = item.name

            // 点击跳转到歌单详情
            root.setClickListener {
                GlobalScope.launch {
                    val songListResponse = KtorHelper.getSongList(item.id)
                    withContext(Dispatchers.Main) {
                        if (songListResponse.code == 200 && (songListResponse.songs?.isNotEmpty() == true)) {
                            DialogHelper.showMusicList(
                                context,
                                songListResponse.songs,
                                item.picUrl,
                                item.name,
                                item.creator.nickname,
                                item.creator.avatarUrl,
                            )
                        } else {
                            context.toast("获取歌单失败")
                        }
                    }
                }

            }

//            if (holder.adapterPosition == 0) {
//                root.performClick()
//            }
        }
    }
}