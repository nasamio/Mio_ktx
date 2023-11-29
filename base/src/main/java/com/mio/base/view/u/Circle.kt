package com.mio.base.view.u

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.view.u.UUtils.getDistance
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 圆形
 */
class Circle(context: Context, attributeSet: AttributeSet?) : UBaseView(
    context, attributeSet,
) {

    var drawText = false

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val radius = min(width, height)
        Log.d(TAG, "onDraw: width: $width, height: $height, radius: $radius")
        canvas?.apply {
            drawCircle(width / 2f, height / 2f, radius / 2f, requirePaint())

            if (!drawText) return
            val text = "ax: $ax, ay: $ay"
            val requirePaint = requirePaint()
            requirePaint.textSize = 20.dp.toFloat()
            requirePaint.color = Color.RED
            drawText(
                text,
                width / 2f - requirePaint.measureText(text) / 2f,
                height / 2f - (requirePaint.descent() + requirePaint.ascent()) / 2f,
                requirePaint
            )
        }


    }

    override fun update(time: Double) {
        super.update(time)
    }

    override fun checkCollision(others: MutableList<View>): UBaseView? {
        others.forEachIndexed { index, view ->
            val other = view as Circle
            val dis = getDistance(this, other)
            val pDis = width / 2f + other.width / 2f
            val isCollision = dis <= (pDis)

//            if (dis < (pDis)) {
//                // 实现碰撞体内部重合的时候，将当前的碰撞体移出重合区域
//                val overlap = pDis - dis
//                val angle = atan2(other.y - y, other.x - x)
//                val xOffset = cos(angle) * overlap
//                val yOffset = sin(angle) * overlap
//
//                // 移出重合区域
//                x -= xOffset.toFloat()
//                y -= yOffset.toFloat()
//            }



            if (isCollision) {
                return view
            }
        }

        return super.checkCollision(others)
    }
}