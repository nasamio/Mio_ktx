package com.mio.music

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.mio.music.data.Music
import java.io.File
import java.security.MessageDigest

fun String.requireMusicName(): String {
    // 使用正则表达式匹配 "." 后的内容，包括中文和英文括号
    val regex = Regex("\\.\\s*([^.]*)")

    val matchResult = regex.find(this)
    val content = matchResult?.groups?.get(1)?.value?.trim() ?: ""

    // 删除中文和英文括号及其内部内容
    return content.replace(Regex("[\\(（][^\\)）]*[\\)）]"), "")
}


fun String.calculateSixDigitNumber(): Int {
    val md5 = MessageDigest.getInstance("MD5")
    md5.update(this.toByteArray(Charsets.UTF_8))
    val digest = md5.digest()

    val hashCode = digest.fold(0) { result, byte ->
        result * 256 + byte
    }

    return (hashCode and 0x00FFFFFF) % 1000000
}

fun String?.parseAudioFileName(path: String): Music? {
    this?.let { fileName ->
        val regex = Regex("(\\d+)\\.\\s*([^（]+)（\\+(.+)）\\..*")
        val matchResult = regex.find(fileName)

        matchResult?.let { result ->
            val id = (fileName.calculateSixDigitNumber()).toLong()
            val title = result.groups[2]?.value ?: ""
            val artist = result.groups[3]?.value ?: ""

            // 获取音频文件时长
            val duration = -1L

            return Music(id, title, artist, "", duration, path)
        }
    }

    return null
}

@SuppressLint("ClickableViewAccessibility")
fun View.setOnPressListener(onPress: (Boolean) -> Unit) {
    this.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下事件
                onPress(true)
            }

            MotionEvent.ACTION_UP -> {
                // 抬起事件
                onPress(false)
            }
        }
        false // 返回 true 表示消费触摸事件
    }
}

fun String.albumStr(): String {
    return this.ifEmpty { "未知专辑" }
}

fun Float.formatToTwoDecimalPlaces(): Float {
    return String.format("%.2f", this).toFloat()
}

fun Double.formatToTwoDecimalPlaces(): Double {
    return String.format("%.2f", this).toDouble()
}