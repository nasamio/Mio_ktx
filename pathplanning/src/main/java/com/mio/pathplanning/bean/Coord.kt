package com.mio.pathplanning.bean

/**
 * 坐标
 */
data class Coord(
    var x: Int,
    var y: Int,
) {
    override fun equals(obj: Any?): Boolean {
        if (obj == null) return false
        return if (obj is Coord) {
            this.x == obj.x && this.y == obj.y
        } else {
            false
        }
    }
}
