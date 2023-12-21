package com.mio.mio_ktx.ui.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class Msg(
    val id: Int,
    val msg: String,
    val time: String,
    val type: Int = 1, // 发送是0 接收是1
) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }

}
