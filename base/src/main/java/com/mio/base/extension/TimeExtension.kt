package com.mio.base.extension

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 把这样的时间字符串（2023-11-22T05:33:54.000+00:00）转成时间戳
 */
fun String.normalTime(): Long {
    // 定义时间格式
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    // 将字符串解析为 ZonedDateTime 对象
    val zonedDateTime = ZonedDateTime.parse(this, formatter)

    // 将 ZonedDateTime 转换为时间戳
    return zonedDateTime.toInstant().toEpochMilli()
}