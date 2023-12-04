package com.mio.base.extension

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 把这样的时间字符串（2023-11-22T05:33:54.000+00:00）转成时间戳
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.normalTime(): Long {
    // 定义时间格式
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    // 将字符串解析为 ZonedDateTime 对象
    val zonedDateTime = ZonedDateTime.parse(this, formatter)

    // 将 ZonedDateTime 转换为时间戳
    return zonedDateTime.toInstant().toEpochMilli()
}

/**
 * 把时间戳转成这样的时间字符串（2023-11-22T05:33:54.000+00:00）
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toNormalTime(): String {
    // 将时间戳转换为 Instant 对象
    val instant = Instant.ofEpochMilli(this)

    // 定义时间格式
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    // 将 Instant 对象转换为 ZonedDateTime 对象
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

    // 将 ZonedDateTime 对象格式化为字符串
    return formatter.format(zonedDateTime)
}
