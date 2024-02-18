package com.mio.enc.bean

class DRField : LRField() {
    val items = mutableListOf<DRDesp>()

    override fun toString(): String {
        return items.joinToString("\n") { it.toString() }
    }
}