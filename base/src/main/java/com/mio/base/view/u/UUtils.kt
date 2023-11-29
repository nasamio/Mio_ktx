package com.mio.base.view.u

import kotlin.math.sqrt

object UUtils {
    val G = 6.674E5 // 6.67 * 10^-11

    /**
     * 计算u2对u1的引力
     */
    fun fBetween(u1: UBaseView, u2: UBaseView): Pair<Double, Double> {
        val distance = getDistance(u1, u2)
        // 引力大小
        val f = (G * u1.mass * u2.mass) / (distance * distance)
        // 引力方向
        val pair = normalizeVector(
            Pair<Double, Double>(
                (u2.x - u1.x).toDouble(), (u2.y - u1.y).toDouble()
            )
        )
        return Pair(pair.first * f, pair.second * f)
    }

    fun normalizeVector(vector: Pair<Double, Double>): Pair<Double, Double> {
        val (x, y) = vector
        val length = Math.sqrt((x * x + y * y).toDouble())

        // 避免除以零，如果长度为零，返回原始向量
        if (length == 0.0) {
            return vector
        }

        val normalizedX = x / length
        val normalizedY = y / length

        return Pair(normalizedX, normalizedY)
    }


    fun getDistance(x1: Double, y1: Double, x2: Double, y2: Double): Float {
        return Math.sqrt(Math.pow((x1 - x2).toDouble(), 2.0) + Math.pow((y1 - y2).toDouble(), 2.0))
            .toFloat()
    }

    fun getDistance(u1: UBaseView, u2: UBaseView): Float {
        return getDistance(u1.cx, u1.cy, u2.cx, u2.cy)
    }

    data class Vector(val x: Double, val y: Double)

    fun calculateCollisionVelocity(
        v1: Vector,
        v2: Vector,
        m1: Double,
        m2: Double,
    ): Pair<Vector, Vector> {
        val v1PrimeX = (v1.x * (m1 - m2) + 2 * m2 * v2.x) / (m1 + m2)
        val v1PrimeY = (v1.y * (m1 - m2) + 2 * m2 * v2.y) / (m1 + m2)

        val v2PrimeX = (v2.x * (m2 - m1) + 2 * m1 * v1.x) / (m1 + m2)
        val v2PrimeY = (v2.y * (m2 - m1) + 2 * m1 * v1.y) / (m1 + m2)

        return Pair(Vector(v1PrimeX, v1PrimeY), Vector(v2PrimeX, v2PrimeY))
    }

    fun getAfterSpeed(
        v1: Double,
        m1: Double,
        v2: Double,
        m2: Double,
    ): Double {
        return (v1 * (m1 - m2) + 2 * m2 * v2) / (m1 + m2)
    }

    fun compositeForce(vx: Double, vy: Double): Double {
        return sqrt(vx * vx + vy * vy)
    }

    data class Velocity(val x: Double, val y: Double)

    fun calculateCollision(
        m1: Double,
        v1x: Double,
        v1y: Double,
        m2: Double,
        v2x: Double,
        v2y: Double,
    ): Pair<Double, Double> {
        // 计算碰撞后小球1的速度
        val v1fx = (m1 * v1x + m2 * v2x - m2 * (v1x - v2x)) / (m1 + m2)
        val v1fy = (m1 * v1y + m2 * v2y - m2 * (v1y - v2y)) / (m1 + m2)

        // 计算碰撞后小球2的速度
        val v2fx = v1fx + (v1x - v2x)
        val v2fy = v1fy + (v1y - v2y)

        return Pair(v1fx, v1fy)
    }
}