package com.mio.base.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import com.mio.base.Tag.TAG
import com.mio.base.view.u.Circle
import com.mio.base.view.u.UUtils
import java.util.Timer
import java.util.TimerTask


class GravityVp(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    val updateInterval = 100
    val mFactor = 0f // 为了让看到的现象更加清晰的系数
    val g = 9.8f * mFactor // 重力加速度
    val e = 0.95f // 碰撞系数

    fun update() {
        // 更新每个item的状态
        children.forEachIndexed { index, child ->
            val view = child as Circle
            val time = updateInterval / 1000.0

            view.ax = 0.0
            view.ay = g.toDouble()

            val others = children.filter { it -> it != child }.toMutableList()
            val other = view.checkCollision(others)
            other?.let {
                // 到这里说明view和it两个view碰撞了
                // 计算碰撞后的速度
                val afterSpeed =
                    UUtils.calculateCollision(view.mass, view.vx, view.vy, it.mass, it.vx, it.vy)
//                Log.d(
//                    TAG,
//                    "update: before: ${view.vx},${view.vy},after: ${afterSpeed.first},${afterSpeed.second * if (view.vy > 0) -1f else 1f}"
//                )
                view.vx = afterSpeed.first * if (view.vx > 0) -1f else 1f // 只考虑相对碰撞,不考虑侧方碰撞
                view.vy = afterSpeed.second * if (view.vy > 0) -1f else 1f// 只考虑相对碰撞,不考虑侧方碰撞

                // 这里考虑异常情况两个出现重叠 直接把当前的“弹出”

            }
            view.update(time)
            view.tag = index.toString()
//            Log.d(TAG, "update: index:${view.tag},speed x: ${view.vx}, y: ${view.vy}")
        }

        // 碰撞检测
        /*        children.forEachIndexed { index, child ->
                    val hasHandled = child.tag as Boolean
                    if (hasHandled) return@forEachIndexed

                    val view = child as Circle
                    val time = updateInterval / 1000.0
                    children.filter { view -> view != child }.forEachIndexed { i, v ->

                        val dis = UUtils.getDistance(child, v as UBaseView)
                        Log.d(TAG, "update: speed x: ${child.vx}, y: ${child.vy}")
                        Log.d(
                            TAG,
                            "update: abs${abs(child.measuredWidth / 2f + v.measuredWidth / 2f - dis)}"
                        )
                        val isCollision = abs(child.measuredWidth / 2f + v.measuredWidth / 2f - dis) < 3f
                        if (isCollision) {
                            // 计算碰撞后的速度
                            Log.d(
                                TAG, "update: before:${
                                    UUtils.compositeForce(
                                        child.vx,
                                        child.vy
                                    )
                                }"
                            )

                            val v1 = UUtils.calculateCollisionVelocity(
                                UUtils.Vector(child.vx, child.vy),
                                UUtils.Vector(v.vx, v.vy),
                                child.mass,
                                v.mass
                            )

                            Log.d(
                                TAG,
                                "update: real before:child:${child.vx},${child.vy},v:${v.vx},${v.vy};" +
                                        "after:child:${v1.first.x},${v1.first.y},v:${v1.second.x},${v1.second.y}"
                            )

                            val factor = view.e * v.e
                            child.vx = v1.first.x * factor * if (child.vx > 0) -1 else 1
                            child.cx += child.vx * time
                            child.vy = v1.first.y * factor * if (child.vy > 0) -1 else 1
                            child.cy += child.vy * time

                            v.vx = v1.second.x * factor * if (v.vx > 0) -1 else 1
                            v.cx += v.vx * time
                            v.vy = v1.second.y * factor * if (v.vy > 0) -1 else 1
                            v.cy += v.vy * time
                            child.tag = true


                            Log.d(TAG, "update: after v1: ${v1.first.x},${v1.first.y}")
                        }
                    }

                    Log.d(TAG, "update: vy: ${view.vy}, cy: ${view.cy},time: $time,dy: ${view.vy * time}")
                }*/
    }

    override fun postInvalidate() {
        children.forEachIndexed { index, v ->
            val view = v as Circle
            // 不能调到地板以下
            if (view.cy + view.measuredHeight / 2 > measuredHeight) {
                view.cy = (measuredHeight - view.measuredHeight / 2f).toDouble()
                view.vy = -view.vy * e * view.e
            }
            // 左右不能掉出去
            if (view.cx - view.measuredWidth / 2 < 0) {
                view.cx = (view.measuredWidth / 2f).toDouble()
                view.vx = -view.vx * e * view.e
            }
            if (view.cx + view.measuredWidth / 2 > measuredWidth) {
                view.cx = (measuredWidth - view.measuredWidth / 2f).toDouble()
                view.vx = -view.vx * e * view.e
            }
            // 上面也不能掉出去
            if (view.cy - view.measuredHeight / 2 < 0) {
                view.cy = (view.measuredHeight / 2f).toDouble()
                view.vy = -view.vy * e * view.e
            }
        }
        super.invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEachIndexed { index, view ->
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
//            child.layout(0, 0, 100.dp, 100.dp)
//            Log.d(Tag.TAG, "onLayout: childMeasuredWidth: ${child.measuredWidth}")
            val view = child as Circle
            view.layout(
                (view.cx - view.measuredWidth / 2).toInt(),
                (view.cy - view.measuredHeight / 2).toInt(),
                (view.cx + view.measuredWidth / 2).toInt(),
                (view.cy + view.measuredHeight / 2).toInt()
            )
        }
    }

    val timer: Timer by lazy {
        Timer()
    }

    var isRunning = true
    fun start() {
        isRunning = true
    }

    fun stop() {
        isRunning = false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (!isRunning) return
                update()
                postInvalidate()
                post {
                    requestLayout()
                }
            }

        }, 100, updateInterval.toLong())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.cancel()
    }
}