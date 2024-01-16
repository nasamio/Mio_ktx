package com.mio.mio_ktx.ui.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.mio.base.view.table.MyAnnotation
import com.mio.base.view.table.MyPropertyAnnotation

@MyAnnotation("Some info")
data class Msg(
    @MyPropertyAnnotation("id") val id: Int,
    @MyPropertyAnnotation("msg") val msg: String,
    val time: String,
    val type: Int = 1, // 发送是0 接收是1
) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }

}
