package com.mio.enc.bean

open class LR<H : LRHeader, D : LRDir, F : LRField>(
    var header: H,
    var dir: D,
    var field: F,
) {
    override fun toString(): String {
        return """
            header: 
$header
            dir: 
$dir
            field: 
$field
        """.trimIndent()
    }
}