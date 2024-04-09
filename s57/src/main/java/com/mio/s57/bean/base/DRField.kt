package com.mio.s57.bean.base

class DRField : Field() {
    val data = mutableListOf<L1Data>()

    class L1Data {
        var tag: String = ""

        val data = mutableListOf<L2Data>()
    }

    class L2Data {
        var key: String = ""
        var value: Any = ""
        var isRepeat: Boolean = false
    }
}