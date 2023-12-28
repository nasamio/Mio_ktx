package com.mio.music.data

data class RecommendItem(
    val picUrl: String = "",
    val creator: Creator,
    val trackCount: Int = 0,
    val createTime: Long = 0,
    val playcount: Long = 0,
    val name: String = "",
    val copywriter: String = "",
    val id: Long = 0,
    val type: Int = 0,
    val userId: Long = 0,
    val alg: String = "",
)