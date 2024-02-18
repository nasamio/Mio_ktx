package com.mio.enc.bean

data class LRDirItem(
    var tag: String, // 标识字段
    var fieldLength: Int, // 长度字段
    var fieldPosition: Int, // 定位字段
)
