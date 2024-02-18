package com.mio.enc.bean

import com.mio.enc.helper.S57Helper

class DDRFieldControl(val tag: String = "0001", val parent: DDRFieldControl? = null) {

    val children: MutableList<DDRFieldControl> = mutableListOf()

    override fun toString(): String {
        return S57Helper.displayFc(this, 0)
    }
}