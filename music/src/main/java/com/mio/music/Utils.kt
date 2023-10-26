package com.mio.music

object Utils {
    fun getAlbumStr(str: String): String {
        return str.ifEmpty { "未知专辑" }
    }
}