@file:Suppress("UNUSED_EXPRESSION", "LABEL_NAME_CLASH")

package com.mio.s57

import BytesHelper
import android.util.Log
import com.mio.s57.EncHelper.TAG
import com.mio.s57.bean.base.DDR
import com.mio.s57.bean.base.DDRField
import com.mio.s57.bean.base.DR
import com.mio.s57.bean.base.DRField
import com.mio.s57.bean.base.Directory
import com.mio.s57.bean.base.Field
import com.mio.s57.bean.base.LR
import com.mio.s57.bean.base.Leader

object EncHelper {
    const val TAG = "S57Helper"

    var lrList: MutableList<LR> = mutableListOf()

    fun load(filePath: String) {
        BytesHelper.load(filePath)

        // 头标区
        val ddrLeader = readLeader()
        lrList.add(DDR().apply {
            leader = ddrLeader

            // 目录区
            directory = readDir(ddrLeader)

            // 字段区
            field = readDDRField(ddrLeader)
        })
//        Log.d(TAG, "load: ddrLeader:$ddrLeader")

        var start = ddrLeader.recordLength
        var index = 0
        while (start < BytesHelper.encBytes!!.size) {
            BytesHelper.position = start
            val drLeader = readLeader()
            val drDir = readDir(drLeader)
            lrList.add(DR().apply {


//                if (lrList.size == 4) {
                leader = drLeader
                directory = drDir
                field = readDRField(drLeader, drDir) // 302579
                Log.d(
                    TAG,
                    "load(${index++})($start/${BytesHelper.encBytes!!.size}): drLeader:$drLeader" +
                            "\n drDir:\n$drDir"
                )

//                }
            })

            start += (drLeader.recordLength)
        }
//        Log.d(
//            TAG,
//            "load: header size(${lrList.size}):${lrList.joinToString(separator = "\n") { it.leader.toString() }}"
//        )
//        Log.d(TAG, "load: dir:${lrList.joinToString(separator = "\n") { it.directory.toString() }}")
        BytesHelper.position = ddrLeader.recordLength
    }

    var count = 0
    private fun readDRField(drLeader: Leader, drDir: Directory): Field {
        val start = BytesHelper.position
        Log.e(
            TAG,
            "readDRField: count:${count++} lr count:${lrList.size} start:${BytesHelper.position}"
        )
        val drField = DRField()
        // showNext(100)
        drDir.items.forEach { drDirItem ->
            val ddr = lrList[0]
            val ddrField = ddr.field as DDRField
            ddrField.fieldDesp.find { it.tag == drDirItem.tag }?.let {
                // 起始地址应该是开始地址+各个字段的地址
                BytesHelper.position = start + drDirItem.position

                val isRepeat = it.attributeList[0].isRepeat
                if (isRepeat) { // 重复属性 循环读取

                    drField.data.add(
                        DRField.L1Data().apply {
                            tag = drDirItem.tag

                            var times = (drDirItem.length - 1) / (it.attributeList.sumOf { it.len })
                            val has1 = it.attributeList.count() { it.len == -1 } > 0
                            Log.d(
                                TAG,
                                "readDRField: dirItemLen:${drDirItem.length},sum:${it.attributeList.sumOf { it.len }}"
                            )

                            if (has1) { // 如果存在不定长的字段 只能一个个读 一直读到长度满足diDirItem.len
                                val teminatorLen = if (it.lexicalLevel() == 2) 2 else 1 // 终止符长度
                                var oldPos = BytesHelper.position
                                var len = 0
                                times = 0
                                while (len < drDirItem.length - teminatorLen) {
                                    times++
                                    it.attributeList.forEach {
                                        var fLen = it.len
                                        if (it.len == -1) {
                                            fLen = 0
                                            if ((oldPos != BytesHelper.encBytes!!.size - 1)
                                                && (BytesHelper.encBytes!![oldPos].toInt()
                                                    .toChar() == BytesHelper.UT
                                                        || BytesHelper.encBytes!![oldPos].toInt()
                                                    .toChar() == BytesHelper.FT)
                                            ) {
                                                oldPos++
                                            }
                                            if (teminatorLen == 2) {
                                                while (!(BytesHelper.encBytes!![oldPos + fLen].toInt()
                                                        .toChar() == BytesHelper.UT
                                                            && BytesHelper.encBytes!![oldPos + fLen + 1].toInt() == 0)
                                                ) {
                                                    fLen++
                                                }
                                            } else {
                                                Log.e(
                                                    TAG,
                                                    "readDRField: old:$oldPos,flen:$fLen,tem:$teminatorLen",
                                                )
                                                if (oldPos + fLen >= BytesHelper.encBytes!!.size) return@forEach
                                                while (BytesHelper.encBytes!![oldPos + fLen].toInt()
                                                        .toChar() != BytesHelper.UT
                                                    && BytesHelper.encBytes!!.size > oldPos + fLen
                                                ) {
                                                    Log.e(
                                                        TAG,
                                                        "readDRFieldin: old:$oldPos,flen:$fLen,tem:$teminatorLen",
                                                    )
                                                    fLen++
                                                    if (oldPos + fLen >= BytesHelper.encBytes!!.size) break
                                                }
                                            }
                                            fLen += teminatorLen
                                        }
                                        oldPos += fLen
                                        len += fLen

                                        if (oldPos >= BytesHelper.encBytes!!.size) {
                                            oldPos = BytesHelper.encBytes!!.size - 1
                                        }
                                    }
                                }

                            }
                            repeat(times) { time ->
                                it.attributeList.forEach { attribute ->
                                    var tempV: Any = ""

                                    if (BytesHelper.position + attribute.len >= BytesHelper.encBytes!!.size) {
                                        return@forEach
                                    }

                                    when (attribute.type) {
                                        "b1" -> {
                                            tempV = when (attribute.len) {
                                                1 -> BytesHelper.getByte()
                                                2 -> BytesHelper.getUInt16()
                                                4 -> BytesHelper.getUInt32()
                                                else -> throw IllegalArgumentException("b1 错误的num：${attribute.len}")
                                            }
                                        }

                                        "b2" -> {
                                            tempV = when (attribute.len) {
                                                1 -> BytesHelper.getSByte()
                                                2 -> BytesHelper.getInt16()
                                                4 -> BytesHelper.getInt32()
                                                else -> throw IllegalArgumentException("b2 错误的num：${attribute.len}")
                                            }
                                        }

                                        "A" -> {
                                            if (attribute.len == -1) { // 读取直到停止符
                                                tempV = readUtilUT()
                                                BytesHelper.position++
                                            } else {
                                                tempV =
                                                    BytesHelper.getString(
                                                        attribute.len,
                                                        it.lexicalLevel()
                                                    )
                                            }
                                        }

                                        "B" -> {
                                            tempV = BytesHelper.getBitStr(attribute.len)
                                        }

                                        "I" -> {
                                            tempV = BytesHelper.getInteger(attribute.len)
                                        }

                                        "R" -> {
                                            tempV = BytesHelper.getDouble(attribute.len)
                                        }

                                        "@" -> {
                                            tempV = BytesHelper.getString(attribute.len)
                                        }

                                        else -> {
                                            Log.e(
                                                TAG,
                                                "readDRField: 未识别的类型：${attribute.type}"
                                            )
                                        }
                                    }


                                    Log.d(TAG, "readDRField: test25:${attribute.name}:$tempV")
                                    Log.d(
                                        TAG,
                                        "readDRField: current:${data.joinToString { it.key }}"
                                    )

                                    data.find { it.key == attribute.name }
                                        ?.let { // 说明已有该字段 直接添加
                                            Log.d(TAG, "readDRField: test24:已有字段:${it.key}")
                                            (it.value as MutableList<Any>).add(tempV)
                                        } ?: run { // 说明没有该字段 需要首次添加
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = mutableListOf(tempV)
                                            Log.d(
                                                TAG,
                                                "readDRField: test24:首次添加字段:${key}:$value"
                                            )
                                        })
                                    }

                                }
                            }
                        })


                    Log.d(TAG, "readDRField: test23:${drField.data.size}")
                    drField.data.forEach {
                        Log.d(TAG, "readDRField: test2:${it.tag}:\n${
                            it.data.joinToString(separator = "\n") { it.key + ":" + it.value }
                        }"
                        )
                    }
                } else { // 解析一个一级条目 下属可能有多个二级条目
                    drField.data.add(
                        DRField.L1Data().apply {
                            tag = drDirItem.tag
                            it.attributeList.forEach { attribute ->
                                jumpIfFT()
                                when (attribute.type) {
                                    "b1" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = when (attribute.len) {
                                                1 -> BytesHelper.getByte()
                                                2 -> BytesHelper.getUInt16()
                                                4 -> BytesHelper.getUInt32()
                                                else -> throw IllegalArgumentException("b1 错误的num：${attribute.len}")
                                            }
                                            Log.d(TAG, "readDRField: value2 $key:${value}")
                                        })
                                    }

                                    "b2" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = when (attribute.len) {
                                                1 -> BytesHelper.getSByte()
                                                2 -> BytesHelper.getInt16()
                                                4 -> BytesHelper.getInt32()
                                                else -> throw IllegalArgumentException("b2 错误的num：${attribute.len}")
                                            }
                                        })
                                    }

                                    "A" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            if (attribute.len == -1) { // 读取直到停止符
                                                value = readUtilUT()
                                                BytesHelper.position++
                                            } else {
                                                value =
                                                    BytesHelper.getString(
                                                        attribute.len,
                                                        it.lexicalLevel()
                                                    )
                                            }
                                            Log.d(TAG, "readDRField: value3 $key:${value}")
                                        })
                                    }

                                    "B" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = BytesHelper.getBitStr(attribute.len)
//                                    Log.d(TAG, "readDRField: value4 $key:${value}")
                                        })
                                    }

                                    "I" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = BytesHelper.getInteger(attribute.len)
//                                    Log.d(TAG, "readDRField: value5 $key:${value}")
                                        })
                                    }

                                    "R" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = BytesHelper.getDouble(attribute.len)
                                        })
                                    }

                                    "@" -> {
                                        data.add(DRField.L2Data().apply {
                                            key = attribute.name
                                            value = BytesHelper.getString(attribute.len)
                                        })
                                    }

                                    else -> {
                                        Log.e(TAG, "readDRField: 未识别的类型：${attribute.type}")
                                    }
                                }
//                    }
                            }
                        })

                }
            }
        }
        return drField
    }

    private fun readUtilUT(): String {
        var count = 0
        while (BytesHelper.encBytes!![BytesHelper.position + count].toInt()
                .toChar() != BytesHelper.UT
        ) {
            count++
        }
        return BytesHelper.getString(count)
    }

    private fun readDDRField(ddrLeader: Leader): DDRField {
        val tempField = DDRField()
        jumpIfFT()
        // showNext(100)

        if (BytesHelper.getString(4) == "0000") {
            // 字段控制字段固定以0000开头
            BytesHelper.position += 5 // 这几个字符无含义
        }
        jumpIfUT()

        val temp1 = mutableListOf<Temp1>()
        while (BytesHelper.current() != BytesHelper.FT) {
            val parentTag = BytesHelper.getString(4)
            val childTag = BytesHelper.getString(4)
            temp1.add(Temp1(parentTag, childTag))
        }
        temp1.sortBy { it.parentTag }
        temp1.forEach { tempItem ->
            if (tempItem.parentTag == tempField.fieldControl.tag) {
                tempField.fieldControl.children.add(
                    DDRField.FieldControlField().apply {
                        tag = tempItem.childTag
                    }
                )
            } else {
                tempField.fieldControl.children.find { it.tag == tempItem.parentTag }?.apply {
                    tag = tempItem.parentTag
                    children.add(
                        DDRField.FieldControlField().apply {
                            tag = tempItem.childTag
                            parent = this
                        }
                    )
                }
            }
        }
//        Log.d(TAG, "readField: \n${log(field.fieldControl)})")

        // 深度优先遍历field control
        val fieldControlTagList: MutableList<String> = mutableListOf()
        dfsTraversal(tempField.fieldControl, fieldControlTagList)
        var fIndex = 0

        // 数据描述字段
        while (BytesHelper.position < ddrLeader.recordLength) {
            val dataDespField = DDRField.DataDespField()
            jumpIfFT()
            dataDespField.apply {
                tag = fieldControlTagList[fIndex]

                dataStructCode = BytesHelper.getInteger(1)
                dataTypeCode = BytesHelper.getInteger(1)
                supControlCode = BytesHelper.getString(2)
                printSymbols = BytesHelper.getString(2)
                truncatedEscapeSequence = BytesHelper.getString(3)

                // 字段名
                var count = 0
                while (BytesHelper.encBytes!![BytesHelper.position + count].toInt().toChar()
                    != BytesHelper.UT
                ) {
                    count++
                }
                fieldName = BytesHelper.getString(count)
                jumpIfUT()
                count = 0
                // 属性列表
                while (BytesHelper.encBytes!![BytesHelper.position + count].toInt().toChar()
                    != BytesHelper.UT
                ) {
                    count++
                }
                var attrStr = BytesHelper.getString(count)

                while (attrStr.length >= 4) {
                    dataDespField.attributeList.add(
                        DDRField.DataDespField.DataDesp().apply {
                            name = attrStr.substring(attrStr.length - 4)
                            isRepeat = attrStr.length > 4 && attrStr.substring(attrStr.length - 5)
                                .contains("*")
                        }
                    )
                    if (attrStr.length > 4) attrStr = attrStr.substring(0, attrStr.length - 5)
                    else if (attrStr.length == 4) attrStr = ""
                }
                if (dataDespField.attributeList.isEmpty()) {
                    dataDespField.attributeList.add(
                        DDRField.DataDespField.DataDesp().apply {
                            name = "RCID"
                        }
                    )
                }

                dataDespField.attributeList.reverse() // 反转

//                Log.d(
//                    TAG,
//                    "attrList:${dataDespField.attributeList.joinToString { it.name + " " + it.isRepeat }}"
//                )

                jumpIfUT()

                // 格式控制列表
                count = 0
                while (BytesHelper.encBytes!![BytesHelper.position + count].toInt().toChar()
                    != BytesHelper.FT
                ) {
                    count++
                }
                var formatStr = BytesHelper.getString(count)
                // 去除两侧括号
                if (formatStr.startsWith("(") && formatStr.endsWith(")")) {
                    formatStr = formatStr.substring(1, formatStr.length - 1)
                }
//                Log.d(TAG, "readField: formatStr:$formatStr")

                val formatList = mutableListOf<Temp2>()
                formatStr.split(",").forEach { str2 ->
                    val str = str2.replace("(", "").replace(")", "")

                    var repeat = 1
                    var tag = ""
                    var len = -1

                    if (str.length == 1) {
                        repeat = 1
                        tag = str
                        len = -1
                    } else {
                        // 找到第一个不为数字的index
                        var index = 0
                        while (index < str.length && str[index].isDigit()) {
                            index++
                        }
                        repeat = str.substring(0, index).let { if (it.isEmpty()) 1 else it.toInt() }
                        if (str[index] == 'A'
                            || str[index] == 'B'
                            || str[index] == 'I'
                            || str[index] == 'R'
                            || str[index] == '@'
                        ) {
                            tag = str[index].toString()
                            len = str.substring(index + 1)
                                .let { if (it.isEmpty()) -1 else it.toInt() }
                        } else {
                            tag = str.substring(index, index + 2)
                            len = str.substring(index + 2)
                                .let { if (it.isEmpty()) -1 else it.toInt() }
                        }

                    }
                    repeat(repeat) {
                        formatList.add(Temp2(tag, len))
                    }
                }
//                Log.d(
//                    TAG,
//                    "readField: format list:${formatList.joinToString { it.tag + " " + it.len }}"
//                )
                dataDespField.attributeList.forEachIndexed { index, item ->
                    item.apply {
                        type = formatList[index].tag
                        len = formatList[index].len
                    }
                }


                jumpIfFT()
            }


            tempField.fieldDesp.add(dataDespField)
            fIndex++
        }

        var i = 0
        if (i < 2) {
            Log.d(
                TAG, "readFieldres:\n " +
                        "${
                            tempField.fieldDesp.joinToString {
                                "\n ${i++} " +
                                        it.tag + " " +
                                        it.dataStructCode +
                                        it.dataTypeCode +
                                        it.supControlCode +
                                        it.printSymbols +
                                        it.truncatedEscapeSequence + " " +

                                        it.fieldName + "   " +
                                        it.attributeList.joinToString { "\n   |-" + it.name + " " + it.type + " " + it.len + " " + it.isRepeat }
                            }
                        }\n"
            )
        }

        return tempField
    }

    fun dfsTraversal(root: DDRField.FieldControlField, result: MutableList<String>) {
        // 添加当前节点的 tag 到结果列表
        result.add(root.tag)

        // 遍历当前节点的子节点
        for (child in root.children) {
            dfsTraversal(child, result)
        }
    }

    private fun log(fieldControl: DDRField.FieldControlField, level: Int = 0): String {
        return "---".repeat(level) + fieldControl.tag + "\n" + fieldControl.children.joinToString(
            separator = ""
        ) {
            log(it, level + 1)
        }
    }
}

data class Temp1(val parentTag: String, val childTag: String)
data class Temp2(val tag: String, val len: Int)

private fun jumpIfFT() {
    if (BytesHelper.current() == BytesHelper.FT) {
        BytesHelper.position++
    }
}

private fun jumpIfUT() {
    if (BytesHelper.current() == BytesHelper.UT) {
        BytesHelper.position++
    }
}

private fun showNext(count: Int) {
    Log.d(TAG, "load: next:${BytesHelper.getString(count)}")
    BytesHelper.position -= count
}

private fun readDir(leader: Leader): Directory {
    val dir = Directory()
    var moveCount = 0

    while (BytesHelper.encBytes!![BytesHelper.position].toChar() != BytesHelper.FT) {
        dir.items.add(
            Directory.Item().apply {
                tag = BytesHelper.getString(leader.filedTagSize)
                length = BytesHelper.getInteger(leader.filedLengthSize)
                position = BytesHelper.getInteger(leader.filedPositionSize)

                moveCount += (leader.filedLengthSize + leader.filedPositionSize + leader.filedTagSize)
            }
        )
    }
    return dir
}

private fun readLeader(): Leader {
    return Leader().apply {
        recordLength = BytesHelper.getInteger(5)
        interchangeLevel = BytesHelper.getInteger(1)
        leaderIdentifier = BytesHelper.getString(1)
        extensionIndicator = BytesHelper.getString(1)
        versionNumber = BytesHelper.getInteger(1)
        applicationIndicator = BytesHelper.getString(1)
        filedControlLength = BytesHelper.getInteger(2)
        baseAddress = BytesHelper.getInteger(5)
        extendedCharacterSetIndicator = BytesHelper.getString(3)
        filedLengthSize = BytesHelper.getInteger(1)
        filedPositionSize = BytesHelper.getInteger(1)
        reversed = BytesHelper.getInteger(1)
        filedTagSize = BytesHelper.getInteger(1)
    }
}


