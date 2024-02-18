package com.mio.enc.helper

import BytesHelper
import BytesHelper.FT
import BytesHelper.UT
import android.util.Log
import com.mio.base.Tag.TAG
import com.mio.enc.bean.DDR
import com.mio.enc.bean.DDRDir
import com.mio.enc.bean.DDRField
import com.mio.enc.bean.DDRFieldControl
import com.mio.enc.bean.DDRFieldDespFormatControl
import com.mio.enc.bean.DDRFieldDespItem
import com.mio.enc.bean.DDRFiledDesp
import com.mio.enc.bean.DDRHeader
import com.mio.enc.bean.DR
import com.mio.enc.bean.DRDesp
import com.mio.enc.bean.DRDespItem
import com.mio.enc.bean.DRDir
import com.mio.enc.bean.DRField
import com.mio.enc.bean.DRHeader
import com.mio.enc.bean.LR
import com.mio.enc.bean.LRDir
import com.mio.enc.bean.LRDirItem
import com.mio.enc.extension.isNum

object S57Helper {
    val data = mutableListOf<LR<*, *, *>>()

    fun init(path: String) {
        BytesHelper.load(path)

        data.clear()

        initDDR()

//        var count = 0
//        while (BytesHelper.position < BytesHelper.encBytes?.size!!) {
//            Log.d(TAG, "init: read count: ${++count}")
//            addDR()
//        }
        // 每一个新的开始点 都能计算出来
        var start = data[0].header.recordLength

//        while (start < BytesHelper.encBytes?.size!!)
        repeat(7)
        {
            addDR()

            start = 0
            data.forEach {
                start += it.header.recordLength
            }
            BytesHelper.position = start
        }

//        Log.d(TAG, "initDRs: pos:${BytesHelper.position} ${BytesHelper.getString(100)}")
    }


    private fun initDDR() {
        // 头标区
        val ddrHeader = DDRHeader().apply {
            recordLength = BytesHelper.getInteger(5)
            exchangeLevel = BytesHelper.getInteger(1)
            leaderIdentifier = BytesHelper.getString(1)
            codeIndicator = BytesHelper.getString(1)
            versionNumber = BytesHelper.getInteger(1)
            applicationIndicator = BytesHelper.getString(1)
            fieldControlLength = BytesHelper.getInteger(2)
            fieldAreaBaseAddress = BytesHelper.getInteger(5)
            exCharacterSetIndicator = BytesHelper.getString(3)
            fieldLengthSize = BytesHelper.getInteger(1)
            fieldPositionSize = BytesHelper.getInteger(1)
            reversed = BytesHelper.getInteger(1)
            fieldIndicatorSize = BytesHelper.getInteger(1)
        }
        // 目录区
        val ddrDir = DDRDir()
        // 目录区的个数等于 （基地址-头标区长度24）/（字段标识符长度+字段长度+字段定位长度）
        val dirCount = (ddrHeader.fieldAreaBaseAddress - 24) /
                (ddrHeader.fieldIndicatorSize + ddrHeader.fieldLengthSize + ddrHeader.fieldPositionSize)
        ddrDir.items.clear()
        for (i in 0 until dirCount) {
            val tag = BytesHelper.getString(ddrHeader.fieldIndicatorSize)
            val fieldLength = BytesHelper.getInteger(ddrHeader.fieldLengthSize)
            val fieldPosition = BytesHelper.getInteger(ddrHeader.fieldPositionSize)
            val item = LRDirItem(tag, fieldLength, fieldPosition)
            ddrDir.items.add(item)
        }
        // 跳过最后的终止符
        BytesHelper.position++
        // 字段区
        val ddrField = DDRField().apply {
            fieldControl = initDDRFieldControl()
            fieldDesp = initDDRFieldDesp(ddrDir)
        }
        // 组装添加到数据中
        data.add(DDR(ddrHeader, ddrDir, ddrField))
    }

    private fun addDR() {
        if (BytesHelper.getChar() != FT) {
            BytesHelper.position--
        }

        // 头标区
        val drHeader = DRHeader().apply {
            recordLength = BytesHelper.getInteger(5)
            exchangeLevel = BytesHelper.getInteger(1)
            leaderIdentifier = BytesHelper.getString(1)
            codeIndicator = BytesHelper.getString(1)
            versionNumber = BytesHelper.getInteger(1)
            applicationIndicator = BytesHelper.getString(1)
            fieldControlLength = BytesHelper.getInteger(2)
            fieldAreaBaseAddress = BytesHelper.getInteger(5)
            exCharacterSetIndicator = BytesHelper.getString(3)
            fieldLengthSize = BytesHelper.getInteger(1)
            fieldPositionSize = BytesHelper.getInteger(1)
            reversed = BytesHelper.getInteger(1)
            fieldIndicatorSize = BytesHelper.getInteger(1)
        }
        // 目录区
        val drDir = DRDir()
        // 目录区的个数等于 （基地址-头标区长度24）/（字段标识符长度+字段长度+字段定位长度）
        val dirCount = (drHeader.fieldAreaBaseAddress - 24) /
                (drHeader.fieldIndicatorSize + drHeader.fieldLengthSize + drHeader.fieldPositionSize)
        drDir.items.clear()
        for (i in 0 until dirCount) {
            val tag = BytesHelper.getString(drHeader.fieldIndicatorSize)
            val fieldLength = BytesHelper.getInteger(drHeader.fieldLengthSize)
            val fieldPosition = BytesHelper.getInteger(drHeader.fieldPositionSize)
            val item = LRDirItem(tag, fieldLength, fieldPosition)
            drDir.items.add(item)
        }
        // 字段区
        val currentPos = BytesHelper.position
        val ddr = data[0] as DDR
        val tempDr = DRField()
        drDir.items.forEach { drItem ->
            BytesHelper.position = currentPos + drItem.fieldPosition + 1

            ddr.field.fieldDesp.values.find { it.dirName == drItem.tag }?.let { ddrFieldDespItem ->
                tempDr.items.add(DRDesp().apply {
                    name = ddrFieldDespItem.dirName
                    values.addAll(ddrFieldDespItem.values.map {
                        DRDespItem().apply {
                            key = it.key
                            value = it.value
                            format = it.format
                            length = it.length
                        }
                    })

                    // 获取三个属性相加在一起的长度
                    var oneLength = 0
                    values.forEach {
                        oneLength += it.length
                    }
                    // 获取dr 目录区标注的应该有的长度
                    val totalLength =
                        drDir.items.find { it.tag == ddrFieldDespItem.dirName }?.fieldLength!! - 1

                    var repeatCount = totalLength / oneLength
                    repeatCount = 1
                    if (repeatCount > 1) {
                        Log.d(TAG, "${ddrFieldDespItem.dirName}: total:$totalLength,one:$oneLength")
                    }

                    repeat(repeatCount) {
                        values.forEach {
                            if (it.length >= 0) {
                                when (it.format) {
                                    "b1",
                                    -> it.value = when (it.length) {
                                        1 -> BytesHelper.getByte()
                                        2 -> BytesHelper.getUInt16()
                                        4 -> BytesHelper.getUInt32()
                                        else -> ""
                                    }

                                    "b2",
                                    -> if (repeatCount > 1) {
                                        if (it.value is MutableList<*>) {
                                            (it.value as MutableList<Any>).add(
                                                when (it.length) {
                                                    1 -> BytesHelper.getSByte()
                                                    2 -> BytesHelper.getInt16()
                                                    4 -> BytesHelper.getInt32()
                                                    else -> ""
                                                }
                                            )
                                        } else {
                                            it.value =
                                                mutableListOf(
                                                    when (it.length) {
                                                        1 -> BytesHelper.getSByte()
                                                        2 -> BytesHelper.getInt16()
                                                        4 -> BytesHelper.getInt32()
                                                        else -> ""
                                                    }
                                                )
                                        }
                                    } else {
                                        it.value = when (it.length) {
                                            1 -> BytesHelper.getSByte()
                                            2 -> BytesHelper.getInt16()
                                            4 -> BytesHelper.getInt32()
                                            else -> ""
                                        }
                                    }

                                    "B",
                                    -> it.value = BytesHelper.getUInt32()

                                    "I",
                                    -> it.value = BytesHelper.getInteger(it.length)

                                    "R",
                                    -> it.value = BytesHelper.getDouble(it.length)

                                    "A",
                                    -> {
                                        if (repeatCount > 1) {
                                            if (it.value is MutableList<*>) {
                                                (it.value as MutableList<String>).add(
                                                    BytesHelper.getString(
                                                        it.length
                                                    )
                                                )
                                            } else {
                                                it.value =
                                                    mutableListOf(BytesHelper.getString(it.length))
                                            }
                                        } else {
                                            it.value = BytesHelper.getString(it.length)
                                        }
                                    }
                                }
                            } else { // 一直读到终止符
                                var count = 0
                                while (BytesHelper.getChar() != UT) {
                                    count++
                                }
                                BytesHelper.position -= (count + 1)
                                it.value = when (it.format) {
                                    "b1",
                                    "b2",
                                    "B",
                                    "I",
                                    -> BytesHelper.getBitStr(count)

                                    "R",
                                    -> BytesHelper.getDouble(count)

                                    "A",
                                    -> BytesHelper.getString(count)

                                    else -> ""
                                }

                                BytesHelper.position++
                            }
                        }
                    }
                })
            }
        }

        tempDr.items.forEach {
            Log.d(
                TAG,
                "initDRs: ${it.name} : ${it.values.joinToString { it.key + ":" + it.value }}"
            )
        }

        // 组装
        data.add(DR(drHeader, drDir, tempDr))
    }

    private fun initDDRFieldControl(): DDRFieldControl {
        // 固定读掉的字段 无意义
        val tag0000 = BytesHelper.getString(4)
        val tagDefault = BytesHelper.getString(5)
        val UT = BytesHelper.getByte()
        // 从这里开始 固定读四个字段 直到FT
        val ddrFieldControl = DDRFieldControl()

        var str = ""
        var current = ddrFieldControl

        while (BytesHelper.getChar().also {
                str += it.toString()
                if (str.length == 8) {
//                    Log.d(TAG, "initDDRFieldControl: ${str.substring(0, 4)} -> ${str.substring(4)}")
                    val head = str.substring(0, 4)
                    val body = str.substring(4)
                    // 如果head一致 则添加到DDRFieldControl.children中
                    if (head == current.tag) {
                        val temp = DDRFieldControl(body, current)
                        current.children.add(temp)
                        current = temp
                    } else {
                        // 去current的父节点找一样的
                        while (current.tag != head) {
                            current = current.parent!!
                        }

                        val temp = DDRFieldControl(body, current)
                        current.children.add(temp)
                        current = temp
                    }

                    str = ""
                }
            } != FT) {
        }

        while (current.tag != "0001") {
            current = current.parent!!
        }

        return current
    }

    private fun initDDRFieldDesp(dir: LRDir): DDRFiledDesp {
        val res = DDRFiledDesp()
        // 0500;&   ISO 8211 Record Identifier(b12)1600;&   Data Set Identification fieldRCNM!RCID!EXPP!INT
        dir.items.forEachIndexed { index, lrDirItem ->  // 除0000
            if (lrDirItem.tag == "0000") return@forEachIndexed
            val item = DDRFieldDespItem(
                dirName = lrDirItem.tag,
                dataConstruction = BytesHelper.getInteger(1),
                dataType = BytesHelper.getInteger(1),
                aidControl = BytesHelper.getString(2),
                canPrint = BytesHelper.getString(2),
                qxzyxl = BytesHelper.getString(3)
            ).apply {
                var count = 0
                while (BytesHelper.getChar() != UT) {
                    count++
                }
                BytesHelper.position -= (count + 1)
                name = BytesHelper.getString(count)
                // 跳过一个间隔符
                BytesHelper.getByte()

                // 第一个列表可能有2个间隔符？
                if (BytesHelper.encBytes!![BytesHelper.position + 1].toInt().toChar() == UT) {
                    BytesHelper.getByte()
                }
//                Log.d(TAG, "initDDRFieldDesp: ${BytesHelper.getString(BytesHelper.position)}")

                // 属性列表
                if (dirName == "0001") { // 对于0001标签，属性列表为空，但默认存在一个RCID属性
                    values.add(DDRFieldDespFormatControl("RCID"))
                } else {
                    count = 0
                    while (BytesHelper.getChar() != UT) {
                        count++
                    }
                    BytesHelper.position -= (count)
                    var valueStr = BytesHelper.getString(count - 1)
                    if (valueStr.indexOf("!") == 3) { // 解决第一个可能出现错乱少字符的情况
                        BytesHelper.position -= count
                        valueStr = BytesHelper.getString(count)
                    }
                    Log.d(TAG, "initDDRFieldDesp3:$dirName: $valueStr")
                    valueStr.split("!").forEach {
                        values.add(DDRFieldDespFormatControl(it))
                    }
                    BytesHelper.getByte()
                }

                // 格式控制列表
                count = 0
                while (BytesHelper.getChar() != FT) {
                    count++
                }
                BytesHelper.position -= (count)
                var str = BytesHelper.getString(count - 1)
                if (str.startsWith("(")) str = str.substring(1)
                if (str.endsWith(")")) str = str.substring(0, str.length - 1)
                val temp = mutableListOf<String>()
                str.split(",").forEach { s ->
                    // 判断字符串是否以数字开头
                    if (s[0].toString().isNum()) {
                        repeat(s[0].toString().toInt()) {
                            temp.add(s.substring(1))
                        }
                    } else {
                        temp.add(s)
                    }
                }

                values.forEachIndexed { index, ddrFieldDespFormatControl ->
                    ddrFieldDespFormatControl.apply {
                        val str = temp[index].replace("(", "").replace(")", "")
                        Log.d(TAG, "initDDRFieldDesp: $ddrFieldDespFormatControl -> $str")
                        var type = ""
                        var len = -1
                        if (str.startsWith("b1")) {
                            type = "b1"
                            len = requireLen(str.substring(2))
                        } else if (str.startsWith("b2")) {
                            type = "b2"
                            len = requireLen(str.substring(2))
                        } else if (str.startsWith("A")) {
                            type = "A"
                            len = requireLen(str.substring(1))
                        } else if (str.startsWith("B")) {
                            type = "B"
                            len = requireLen(str.substring(1))
                        } else if (str.startsWith("I")) {
                            type = "I"
                            len = requireLen(str.substring(1))
                        } else if (str.startsWith("R")) {
                            type = "R"
                            len = requireLen(str.substring(1))
                        } else if (str.startsWith("@")) {
                            type = "@"
                            len = requireLen(str.substring(1))
                        }
                        ddrFieldDespFormatControl.format = type
                        ddrFieldDespFormatControl.length = len
                    }
                }
//                Log.d(
//                    TAG,
//                    "initDDRFieldDesp: now: ${
//                        values.joinToString(separator = "\n") {
//                            it.key + " " + it.format + " " + it.length + " " + it.value
//                        }
//                    }"
//                )
                BytesHelper.getByte()
            }

            res.values.add(item)
        }

        return res
    }

    private fun requireLen(str: String): Int {
        return if (str.isEmpty()) -1 else str.toInt()
    }

    fun displayFc(fc: DDRFieldControl, level: Int): String {
        // Log.d(TAG, "displayTag: ${fc.tag},children size:${fc.children.size}")
        var res = "   ".repeat(level) + "|-" + fc.tag
        if (fc.children.size > 0) {
            fc.children.forEach {
                res += "\n" + displayFc(it, level + 1)
            }
        }

        return res
    }
}