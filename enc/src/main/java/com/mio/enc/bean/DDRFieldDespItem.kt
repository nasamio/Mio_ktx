package com.mio.enc.bean

/**
 * 对应的是 0 0001 0500;& ISO/IEC 8211 Record Identifier
 *       |- NAME b1 2 False
 */
class DDRFieldDespItem(
    val dirName: String, // 目录名
    val dataConstruction: Int, // 数据结构代码 0：单数据 1：线性结构 2：多维结构
    val dataType: Int, // 数据类型代码 0：字符串 1：整形 5：二进制 ：混合类型
    val aidControl: String, // 辅助控制代码 "00"
    val canPrint: String, // ";&"
    val qxzyxl: String, // 词汇级别0：□□□ 词汇级别1：-A□ 词汇级别2：%/A 不同的词汇级别，对应的UT和FT的长度不一样。
) {
    var name: String = "" // 字段名
    val values = mutableListOf<DDRFieldDespFormatControl>() // |- NAME b1 2 False
}