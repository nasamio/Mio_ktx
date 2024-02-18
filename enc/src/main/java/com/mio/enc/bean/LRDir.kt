package com.mio.enc.bean

open class LRDir(
    val items: MutableList<LRDirItem> = mutableListOf(),
) {
    override fun toString(): String {
        return items.joinToString(separator = "\n") { it.tag + " " + it.fieldLength + " " + it.fieldPosition }
    }
}
