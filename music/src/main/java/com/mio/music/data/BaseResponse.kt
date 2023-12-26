package com.mio.music.data

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)
