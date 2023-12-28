package com.mio.music.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.lxj.xpopup.XPopup
import com.mio.base.setClickListener
import com.mio.music.R
import com.mio.music.data.RecommendItem
import com.mio.music.databinding.ItemRecommendBinding
import com.mio.music.loadImage
import com.mio.music.ui.view.SongListPopupView

class RvRecommendAdapter :
    BaseQuickAdapter<RecommendItem, BaseDataBindingHolder<ItemRecommendBinding>>(R.layout.item_recommend) {
    override fun convert(holder: BaseDataBindingHolder<ItemRecommendBinding>, item: RecommendItem) {
        holder.dataBinding?.apply {
            ivBg.loadImage(item.picUrl)
            tvName.text = item.name

            // 点击跳转到歌单详情
            root.setClickListener {
                XPopup.Builder(context)
                    .asCustom(SongListPopupView(context, item))
                    .show()
            }

//            if (holder.adapterPosition == 0) {
//                root.performClick()
//            }
        }
    }
}