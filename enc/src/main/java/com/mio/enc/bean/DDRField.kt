package com.mio.enc.bean

class DDRField : LRField() {
    lateinit var fieldControl: DDRFieldControl
    lateinit var fieldDesp: DDRFiledDesp

    override fun toString(): String {
        return """
            fieldControl:
            $fieldControl
            fieldDesp:
            $fieldDesp
            """.trimIndent()
    }
}
