package com.mio.enc.extension

fun String.isNum(): Boolean {
    return this.matches(Regex("[0-9]+"))
}