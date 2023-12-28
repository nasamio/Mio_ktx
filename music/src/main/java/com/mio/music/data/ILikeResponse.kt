package com.mio.music.data

data class ILikeResponse(val code: Int = 0,
                         val ids: List<Long>?,
                         val checkPoint: Long = 0)