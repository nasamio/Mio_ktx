package com.mio.base.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.OverScroller
import androidx.annotation.CallSuper
import com.mio.base.Tag.TAG

/**
 * 自定义view继承该view即可实现滑动
 * 仅适用于单指滑动
 * 过度滑动会瞬移回弹
 */
open class MioScrollView(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val velocityTracker: VelocityTracker by lazy { VelocityTracker.obtain() }

    private var downX = 0f
    private var downY = 0f
    private val overScroll by lazy { OverScroller(context) }

    var enableHorizontalScroll = true // 是否允许水平滑动
    var enableVerticalScroll = false // 是否允许垂直滑动

    var offsetX = 0f // 表示偏移量 0
    var offsetY = 0f // 表示偏移量 0

    var minOffsetX = 0f // 表示最小偏移量 0
    var maxOffsetX = 0f // 表示最大偏移量 0
    var minOffsetY = 0f // 表示最小偏移量 0
    var maxOffsetY = 0f // 表示最大偏移量 0


    @CallSuper
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        velocityTracker.addMovement(event)
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                velocityTracker.clear()
                velocityTracker.addMovement(event)
            }

            MotionEvent.ACTION_MOVE -> {
                offsetX += event.x - downX
                downX = event.x

                offsetY += event.y - downY
                downY = event.y
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker.computeCurrentVelocity(1000)
                val xVelocity = velocityTracker.xVelocity
                val yVelocity = velocityTracker.yVelocity

                if (offsetX < minOffsetX) {
                    offsetX = minOffsetX
                } else if (offsetX > maxOffsetX) {
                    offsetX = maxOffsetX
                }

                overScroll.fling(
                    offsetX.toInt(),
                    offsetY.toInt(),
                    xVelocity.toInt(),
                    yVelocity.toInt(),
                    minOffsetX.toInt(),
                    maxOffsetX.toInt(),
                    minOffsetY.toInt(),
                    maxOffsetY.toInt()
                )

                // handleOverScrollEffect()
                invalidate()
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            if (!enableHorizontalScroll) offsetX = 0f
            if (!enableVerticalScroll) offsetY = 0f
            canvas.translate(offsetX, 0f)
            canvas.translate(0f, offsetY)
        }
    }

    override fun computeScroll() {
        if (overScroll.computeScrollOffset()) {
            offsetX = overScroll.currX.toFloat()
            offsetY = overScroll.currY.toFloat()
            invalidate()
        }
    }
}