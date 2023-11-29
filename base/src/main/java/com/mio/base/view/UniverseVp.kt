package com.mio.base.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.core.view.children
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.view.u.Circle
import com.mio.base.view.u.UBaseView
import com.mio.base.view.u.UUtils
import java.util.Timer
import java.util.TimerTask

class UniverseVp(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEachIndexed { index, view ->
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEachIndexed { index, child ->
//            child.layout(0, 0, 100.dp, 100.dp)
            Log.d(TAG, "onLayout: childMeasuredWidth: ${child.measuredWidth}")
            val view = child as Circle
            view.layout(
                (view.cx - view.measuredWidth / 2).toInt(),
                (view.cy - view.measuredHeight / 2).toInt(),
                (view.cx + view.measuredWidth / 2).toInt(),
                (view.cy + view.measuredHeight / 2).toInt()
            )
        }
    }

    /**
     * 每一帧触发
     */
    fun update() {
        children.forEachIndexed { index, child ->
            val view = child as Circle
            // 计算别的物体对当前物体的合力
            var compositeXF = 0.0
            var compositeYF = 0.0
            children.filter { view -> view != child }.forEachIndexed { i, v ->

                // 判断是否碰撞
                val isCollision = UUtils.getDistance(
                    child,
                    v as UBaseView
                ) <= child.measuredWidth / 2 + v.measuredWidth / 2
                if (isCollision) {
                    // 计算碰撞后的速度
                    val (v1, v2) = UUtils.calculateCollisionVelocity(
                        UUtils.Vector(child.vx, child.vy),
                        UUtils.Vector(v.vx, v.vy),
                        child.mass,
                        v.mass
                    )
                    child.vx = v1.x * child.e
                    child.vy = v1.y * child.e
                    v.vx = v2.x * v.e
                    v.vy = v2.y * v.e
                }

                val fBetween = UUtils.fBetween(child, v as UBaseView)
                // Log.d(TAG, "update:index:$index, fBetween: $fBetween")
                compositeXF += fBetween.first
                compositeYF += fBetween.second
            }
            val compositeForce = Pair<Double, Double>(0.0, 0.0)
            // 计算加速度
            val ax = compositeXF / child.mass
            val ay = compositeYF / child.mass
            // 计算速度
            val vx = view.vx + ax
            val vy = view.vy + ay
            // 计算位移
            val cx = view.cx + vx
            val cy = view.cy + vy
            // 更新
            view.vx = vx
            view.vy = vy
            view.ax = ax
            view.ay = ay
            view.cx = cx
            view.cy = cy
            if (index == 0) {
                Log.d(TAG, "update: cx:$cx, cy: $cy, vx: $vx, vy: $vy, ax: $ax, ay: $ay")
            }
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

        }, 100, 100)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timer.cancel()
    }

}