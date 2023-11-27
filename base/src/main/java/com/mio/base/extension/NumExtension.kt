package com.mio.base.extension

fun Float.devimals(num: Int): Float {
    return String.format("%.${num}f", this).toFloat()
}