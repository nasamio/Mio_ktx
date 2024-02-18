package com.mio.enc.bean

class DRDespItem {
    var key: String = ""
    var value: Any = ""
    var length: Int = -1
    var format: String = ""

    override fun toString(): String {
        return "     $key: $value"
    }
}