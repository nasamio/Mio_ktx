package com.mio.music.data

data class RecommendBean(val haveRcmdSongs: Boolean = false,
                         val code: Int = 0,
                         val featureFirst: Boolean = false,
                         val recommend: List<RecommendItem>?)