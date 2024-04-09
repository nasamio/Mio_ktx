import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import kotlin.experimental.or

object BytesHelper {
    var position = 0 // 存储当前解析到的位置
    var encBytes: ByteArray? = null // 存储的电子海图数据

    // 字符集级别
    private val asciiEncoding = Charset.forName("US-ASCII")
    private val latin1Encoding = Charset.forName("ISO-8859-1")
    private val unicodeEncoding = Charset.forName("UTF-16")

    val UT = 0x1F.toChar() // 分隔
    val FT = 0x1E.toChar() // 终止

    /**
     * 加载电子海图及其更新文件
     */
    fun load(filePath: String) {
        val file = File(filePath)
        val inputStream = FileInputStream(file)
        val fileSize = file.length().toInt()
        encBytes = ByteArray(fileSize)
        inputStream.read(encBytes)
        inputStream.close()
        position = 0
    }

    /**
     * 读取字节为字符串
     */
    fun getString(len: Int, lexicalLevel: Int = 0): String {
        when (lexicalLevel) {
            0 -> String(encBytes!!, position, len, asciiEncoding)
            1 -> String(encBytes!!, position, len, latin1Encoding)
            else -> String(encBytes!!, position, len, unicodeEncoding)
        }.let {
            position += len
            return it
        }
    }

    /**
     * 读取一个字符
     */
    fun getChar(): Char {
        encBytes!![position].toChar()
            .let {
                position++
                return it
            }
    }

    /**
     * 读取字节为整数
     */
    fun getInteger(len: Int): Int {
        var num = 0
        for (j in 0 until len) {
            num = num * 10 + (encBytes!![position] - '0'.toInt())
            position++
        }
        return num
    }

    /**
     * 读取字节为浮点数
     */
    fun getDouble(len: Int): Double {
        val string = getString(len)
        return try {
            string.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    /**
     * 读取二进制位串为长整型
     */
    fun getBitStr(len: Int): ULong {
        var res: ULong = 0u
        for (i in 0 until len) {
            res = res or ((encBytes!![position].toUByte().toULong() shl (i * 8)))
            position++
        }
        return res
    }

    /**
     * 读取1字节为无符号整型
     */
    fun getByte(): Byte {
        return encBytes!![position++]
    }

    /**
     * 读取2字节为无符号整型
     */
    fun getUInt16(): UInt {
        return if (checkPos()) (encBytes!![position++].toUByte()
            .toUInt() or (encBytes!![position++].toUByte()
            .toUInt() shl 8)) else 0u
    }

    private fun checkPos(): Boolean {
        return position < encBytes!!.size
    }

    /**
     * 读取4字节为无符号整型
     */
    fun getUInt32(): UInt {
        return (encBytes!![position++].toUByte().toUInt() or
                (encBytes!![position++].toUByte().toUInt() shl 8) or
                (encBytes!![position++].toUByte().toUInt() shl 16) or
                (encBytes!![position++].toUByte().toUInt() shl 24))
    }

    /**
     * 读取1字节为有符号整型
     */
    fun getSByte(): Byte {
        return encBytes!![position++]
    }

    /**
     * 读取2字节为有符号整型
     */
    fun getInt16(): Short {
        return (encBytes!![position++].toShort() or ((encBytes!![position++].toShort()
            .toInt() shl 8).toShort()))
    }

    /**
     * 读取3字节为有符号整型
     */
    fun getInt32(): Int {
        return (encBytes!![position++].toByte().toInt() or
                (encBytes!![position++].toByte().toInt() shl 8) or
                (encBytes!![position++].toByte().toInt() shl 16) or
                (encBytes!![position++].toByte().toInt() shl 24))
    }

    fun next(): Char {
        return encBytes!![position + 1].toChar()
    }

    fun current(): Char {
        return encBytes!![position].toChar()
    }
}