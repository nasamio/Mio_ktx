package com.mio.s57.bean.base

class Directory {
    val items: MutableList<Item> = mutableListOf()

    class Item {
        var tag: String = ""
        var length: Int = 0
        var position: Int = 0
    }

    override fun toString(): String {
        return items.joinToString(separator = "\n") {
            it.tag + ":" + it.length + ":" + it.position
        }
    }
}