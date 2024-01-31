package com.mio.game

import kotlin.math.pow
import kotlin.math.sqrt

object Utils {
    fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2))
    }
}