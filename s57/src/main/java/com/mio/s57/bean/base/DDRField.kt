package com.mio.s57.bean.base

class DDRField:Field() {
    var fieldControl: FieldControlField = FieldControlField()
    var fieldDesp: MutableList<DataDespField> = mutableListOf<DataDespField>()

    class FieldControlField {
        var tag: String = "0001"
        var parent: FieldControlField? = null
        val children: MutableList<FieldControlField> = mutableListOf()
    }

    class DataDespField {
        var tag = ""

        // 字段控制
        var dataStructCode: Int = 0 // 数据结构代码 “0”：单数据项 “1”：线性结构 “2”：多维结构
        var dataTypeCode: Int = 0 // 数据类型代码 “0”：字符串 “1”：整型 “5”：二进制形式 “6”：混合类型
        var supControlCode: String = "" // 辅助控制码
        var printSymbols: String = "" // 可打印字符
        var truncatedEscapeSequence: String = "" // 截取转义序列 词汇级别0：□□□ 词汇级别1：-A□ 词汇级别2：%/A

        // 字段名
        var fieldName: String = "" // ISO 8211 Record Identifier

        // 属性列表 + 格式控制列表
        var attributeList: MutableList<DataDesp> = mutableListOf()
        fun lexicalLevel(): Int {
            return if (truncatedEscapeSequence == "%/A") {
                2
            } else if (truncatedEscapeSequence == "-A ") {
                1
            } else 0
        }

        class DataDesp {
            var name: String = "" // RCID
            var type: String = "" // b1
            var content: String = ""
            var isRepeat: Boolean = false
            var len: Int = -1
        }
    }
}