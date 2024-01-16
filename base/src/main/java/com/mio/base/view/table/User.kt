package com.mio.base.view.table

import android.graphics.Color
import com.mio.base.dp

@MioBean(
    title = "用户表",
)
data class User(
    @MioColumn(index = 0, title = "Id", width = 40)
    val id: Int,
    @MioColumn(index = 1, title = "名字", width = 100)
    val name: String,
    @MioColumn(index = 2, title = "密码", width = 100)
    val pwd: String,

    val desp: String = "desp",
    val desp2: String = "desp2",
    val desp3: String = "desp3",
    val desp4: String = "desp4",
    @MioColumn(index = 3, title = "颜色", width = 60, type = CellType.Color)
    val color: Int = 1,
    @MioColumn(index = 4, title = "锁", width = 60, type = CellType.Image)
    var isLock: Boolean = false,
)