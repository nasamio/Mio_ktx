package com.mio.enc.bean

class DRDesp {
    var name: String = ""
    val values = mutableListOf<DRDespItem>()

    override fun toString(): String {
        return name + "\n" + values.joinToString(separator = "\n") { it.toString() }
    }
}