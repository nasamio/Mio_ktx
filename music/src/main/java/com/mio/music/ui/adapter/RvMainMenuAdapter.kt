package com.mio.music.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mio.music.R
import com.mio.music.databinding.ItemRvMainMenuBinding

class RvMainMenuAdapter :
    BaseQuickAdapter<RvMainMenuAdapter.RvMainMenuItem, BaseDataBindingHolder<ItemRvMainMenuBinding>>(
        R.layout.item_rv_main_menu
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemRvMainMenuBinding>,
        item: RvMainMenuItem,
    ) {
        holder.dataBinding?.apply {
            ivIcon.setImageResource(item.icon)
            tvTitle.text = item.title
        }
    }

    data class RvMainMenuItem(val title: String, val icon: Int)
}