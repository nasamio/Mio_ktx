package com.mio.filemanager.bean

data class RFile(
    val path: String,
    val isDir: Boolean,
) {
    var name: String = ""
}
