package com.mio.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.extension.drawDialog
import kotlin.math.abs
import kotlin.math.max

/**
 * todo 点击放大
 */
class LineChartView(context: Context, attributeSet: AttributeSet) :
    MioScrollView(context, attributeSet) {
    lateinit var paint: Paint
    lateinit var bgPaint: Paint
    lateinit var textPaint: Paint
    lateinit var checkPaint: Paint
    var marginX = 10.dp
    var marginY = 30.dp // 上下边距 需要给底部文字留出距离

    val showCount = 8
    val textSize: Int = 20.dp
    var dx = 0f
    var dy = 0f
    var sx = 0f

    var sy = 0f
    var checkPos: Int = -1

    private val dialogTextSize: Int = 10.dp

    var data = mutableListOf<LineChartData>(
        LineChartData(0f, "3.15"),
        LineChartData(800f, "3.16"),
        LineChartData(200f, "3.17"),
        LineChartData(300f, "3.18"),
        LineChartData(100f, "3.19"),
        LineChartData(70f, "3.20"),
        LineChartData(130f, "3.21"),
        LineChartData(210f, "3.22"),
        LineChartData(120f, "3.23"),
        LineChartData(0f, "3.24"),
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

        textPaint = Paint()
        textPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            textSize = 12.dp.toFloat()
            paint.color = context.getColor(R.color.black)
        }

        checkPaint = Paint()
        checkPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2.dp.toFloat()
            paint.color = context.getColor(R.color.lcv_check)
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
            var checkData: LineChartData? = null
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

                // 绘制下方文字
                if (index % 2 == 0) {
                    drawText(
                        lineChartData.des,
                        sx + dx * index - textPaint.measureText(lineChartData.des) / 2,
                        sy + textSize,
                        textPaint
                    )
                }

                // 绘制选中
                if (index == checkPos) {
                    checkData = lineChartData
                }

            }
            val bgPath = Path(path)
            bgPath.close()
            drawPath(bgPath, bgPaint)

            // 解决可能会黑
            paint.color = Color.parseColor("#D8818D")
            drawPath(path, paint)

            // 绘制选中
            checkPaint.color = context.getColor(R.color.lcv_check)
            checkData?.let { data ->
                drawLine(
                    sx + dx * checkPos,
                    sy,
                    sx + dx * checkPos,
                    sy - data.data * dy,
                    checkPaint
                )
                drawLine(
                    sx,
                    sy - data.data * dy,
                    width.toFloat(),
                    sy - data.data * dy,
                    checkPaint
                )
                showDialog(
                    canvas,
                    data.des + "\n支出:${data.data}",
                    sx + dx * checkPos,
                    sy - data.data * dy
                )
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun showDialog(canvas: Canvas, text: String, centerX: Float, centerY: Float) {
        canvas.drawDialog(
            text,
            centerX = centerX,
            centerY = centerY,
        )
    }

    public data class LineChartData(val data: Float, val des: String)

    var downX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        // 点击选中
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
            }

            MotionEvent.ACTION_UP -> {
                if (abs(event.x - downX) < dx / 2f) {
                    // 点击
                    findNearestPos(event.x).let {
                        checkPos = it
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    /**
     * 从横坐标中找到最近的index
     */
    private fun findNearestPos(x: Float): Int {
        val temp = mutableListOf<Float>()
        val temp2 = mutableListOf<Float>()

        data.forEachIndexed { index, num ->
            temp.add((sx + index * dx))
            temp2.add((sx + index * dx))
        }
        // Log.d(TAG, "findNearestPos:touch x:$x,temp:$temp")
        temp.sortBy { abs(x - it) }
        // Log.d(TAG, "findNearestPos2:touch x:$x,temp:$temp")
        return temp2.indexOf(temp[0])
    }
}