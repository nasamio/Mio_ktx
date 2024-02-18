package com.mio.enc.bean

class DDRFiledDesp {
    val values = mutableListOf<DDRFieldDespItem>()

    override fun toString(): String {
        return values.joinToString(separator = "\n") {
            it.dirName + " " + it.dataConstruction + it.dataType + it.aidControl + it.canPrint + it.qxzyxl + "   " + it.name + "\n" +
                    it.values.joinToString(separator = "\n") { "   |- ${it.key} ${it.format} ${it.length} ${it.value}" }
        }
    }
}