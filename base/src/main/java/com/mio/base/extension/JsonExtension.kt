package com.mio.base.extension

import com.google.gson.Gson

/**
 * 对象转json
 */
fun Any.toJson(): String {
    return Gson().toJson(this)
}

/**
 * json转对象
 */
fun <T> String.toBean(clazz: Class<T>): T {
    return Gson().fromJson(this, clazz)
}
