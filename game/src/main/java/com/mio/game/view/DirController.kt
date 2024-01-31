package com.mio.game.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.mio.base.Tag.TAG
import com.mio.game.Utils
import kotlin.math.min

/**
 * 方向控制器 尽量只支持方形的
 * 回调矩阵 (-1,-1) (1,1)
 */
class DirController(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val outerPaint by lazy {
        Paint().apply {
            color = Color.GRAY
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private val innerPaint by lazy {
        Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    // 中心的坐标
    val cx: Float by lazy { width / 2f }
    val cy: Float by lazy { height / 2f }
    val outerRadius by lazy { (min(width, height) / 2).toFloat() }

    var ix: Float = 0f
    var iy: Float = 0f
    var innerRadius: Float = 0f

    val outInnerWidth = 5f // 内外圆间距
    var onDirChangeListener: OnDirChangeListener? = null
    private var showCallback = false
    val callbackDuration = 16L // 手柄采样率

    init {
        postDelayed({ judgeCallback() }, callbackDuration)
    }


    private fun judgeCallback() {
        if (showCallback) {
            onDirChangeListener?.let {
                val dx = (ix - cx) / (outerRadius - innerRadius - outInnerWidth)
                val dy = (iy - cy) / (outerRadius - innerRadius - outInnerWidth)
                it.onChange(dx, dy)
            }
        }

        postDelayed({ judgeCallback() }, callbackDuration)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ix = cx
        iy = cy
        innerRadius = outerRadius * .6f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(cx, cy, outerRadius, outerPaint)

        canvas.drawCircle(ix, iy, innerRadius, innerPaint)
    }

    var lx: Float = 0f
    var ly: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val dis = Utils.distance(cx, cy, event?.x!!, event.y)
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    showCallback = true
                    if (dis > outerRadius) { // 如果一开始就点到外圈外 直接不处理
                        return false
                    } else if (dis > outerRadius - innerRadius) {
                        updateIxIy()
                    } else {
                        ix = it.x
                        iy = it.y
                    }
                    lx = it.x
                    ly = it.y

                    invalidate()
                }

                MotionEvent.ACTION_MOVE,
                -> {
                    ix = it.x
                    iy = it.y

                    if (dis > outerRadius - innerRadius) {
                        // 如果大了 需要应谁到对应的直径位置
                        updateIxIy()
                    }

                    invalidate()
                }

                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL,
                -> {
                    ix = cx
                    iy = cy
                    invalidate()

                    showCallback = false
                }
            }
        }
        return true
    }

    private fun updateIxIy() {
        // 计算触摸点与中心点之间的向量
        val dx = ix - cx
        val dy = iy - cy

        // 计算向量与正右方向之间的夹角
        val angle = Math.atan2(dy.toDouble(), dx.toDouble())

        // 计算对应的 cos 和 sin 值
        val cosValue = Math.cos(angle)
        val sinValue = Math.sin(angle)
        // 角度制
        val degree = angle * 180.0 / Math.PI

        ix = cx + (outerRadius - innerRadius - outInnerWidth) * cosValue.toFloat()
        iy = cy + (outerRadius - innerRadius - outInnerWidth) * sinValue.toFloat()
    }


    interface OnDirChangeListener {
        fun onChange(x: Float, y: Float)
    }
}