package com.mio.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.mio.base.dp

class LineChartView(context: Context, attributeSet: AttributeSet) :
    MioScrollView(context, attributeSet) {
    lateinit var paint: Paint
    lateinit var bgPaint: Paint

    var marginX = 10.dp
    var marginY = 10.dp
    val showCount = 8
    var dx = 0f
    var dy = 0f

    var sx = 0f
    var sy = 0f


    var data = mutableListOf<LineChartData>(
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(800f, "3.16"),
        LineChartData(200f, "3.17"),
        LineChartData(300f, "3.18"),
        LineChartData(100f, "3.19"),
        LineChartData(0f, "3.20"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(800f, "3.16"),
        LineChartData(200f, "3.17"),
        LineChartData(300f, "3.18"),
        LineChartData(100f, "3.19"),
        LineChartData(0f, "3.20"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(800f, "3.16"),
        LineChartData(200f, "3.17"),
        LineChartData(300f, "3.18"),
        LineChartData(100f, "3.19"),
        LineChartData(0f, "3.20"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(0f, "3.15"),
        LineChartData(800f, "3.16"),
        LineChartData(200f, "3.17"),
        LineChartData(300f, "3.18"),
        LineChartData(100f, "3.19"),
        LineChartData(0f, "3.20"),
    )
        set(value) {
            field = value
            val max = data.maxBy { it.data }
            max.data.let {
                dy = (height - marginY * 2) / it
            }
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        sx = marginX.toFloat()
        sy = (h - marginY).toFloat()
        dx = (w - marginX * 2) / showCount.toFloat()

        paint = Paint()
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2.dp.toFloat()
            paint.color = Color.parseColor("#D8818D")
        }

        bgPaint = Paint()
        bgPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            shader = LinearGradient(
                0f,
                0f,
                0f,
                height.toFloat(),
                Color.parseColor("#FAD3D0"),
                Color.parseColor("#FFFFFF"),
                android.graphics.Shader.TileMode.CLAMP
            )
        }


        val max = data.maxBy { it.data }
        max.data.let {
            dy = (height - marginY * 2) / it
        }

        isClickable = true
        minOffsetX = -((dx * (data.size - showCount - 1)).toInt()).toFloat()
        maxOffsetX = 0f
        minOffsetY = 0f
        maxOffsetY = height.toFloat()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {

            val path = Path()
            data.forEachIndexed { index, lineChartData ->
                if (index == 0) {
                    path.moveTo(sx, sy - lineChartData.data)
                } else {
                    val lastItem = data[index - 1]
                    if (lastItem.data == lineChartData.data) {
                        path.lineTo(sx + dx * index, sy - lineChartData.data * dy)
                    } else {
                        val targetX = sx + dx * index
                        val targetY = sy - lineChartData.data * dy
                        val factor = .618f
                        path.cubicTo(
                            sx + dx * (index - 1 + factor),
                            sy - lastItem.data * dy,

                            sx + dx * (index - factor),
                            sy - lineChartData.data * dy,
                            targetX,
                            targetY
                        )

                    }
                }
            }
            val bgPath = Path(path)
            bgPath.close()
            drawPath(bgPath, bgPaint)


            drawPath(path, paint)
        }
    }


    public data class LineChartData(val data: Float, val des: String)


}