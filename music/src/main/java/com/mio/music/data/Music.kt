package com.mio.music.data

data class Music(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
) {
    var icRes: Int = 0
}