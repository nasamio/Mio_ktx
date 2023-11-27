package com.mio.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mio.base.R
import com.mio.base.Tag.TAG
import com.mio.base.dp
import com.mio.base.extension.devimals
import kotlin.math.cos
import kotlin.math.sin

class RingGraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    lateinit var ringPaint: Paint
    lateinit var textPaint: Paint
    lateinit var bgPaint: Paint
    lateinit var centerTextPaint: TextPaint

    var bgColor = context.getColor(R.color.white)

    // 中心点
    var cx = 0f
    var cy = 0f

    // 半径
    var radius = 0f

    // 圆环宽度
    var ringWidth = 20.dp

    var margin = 30.dp

    var realStartAngle = 270f

    var line1Len = 15.dp
    var line2Len = 20.dp
    var textMarginLine = 10.dp

    var centerText: String = ""
        set(value) {
            field = value
            invalidate()
        }

    // 圆环颜色 size必须大于等于data的size
    var ringColor = mutableListOf<Int>(
        Color.parseColor("#01C5B5"),
        Color.parseColor("#FC6D78"),
        Color.parseColor("#FBB033"),
        Color.parseColor("#44CAE6"),
        Color.RED,
        Color.GREEN,
        Color.YELLOW,
        Color.CYAN,
        Color.MAGENTA,
        Color.BLUE,
        Color.GRAY,
        Color.LTGRAY,
        Color.DKGRAY,
        Color.BLACK,
        Color.WHITE,
    )

    var data = mutableListOf<RingData>()
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        ringPaint = Paint()
        ringPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = ringWidth.toFloat()
            color = ringColor[0]
        }

        bgPaint = Paint()
        bgPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = bgColor
        }

        textPaint = Paint()
        textPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1.dp.toFloat()
            color = context.getColor(R.color.black_30)
            textSize = 20.dp.toFloat()
        }
        centerTextPaint = TextPaint()
        centerTextPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeWidth = 1.dp.toFloat()
            color = context.getColor(R.color.black_30)
            textSize = 20.dp.toFloat()
        }

        cx = w / 2f
        cy = h / 2f
        radius = (h) / 2f - ringWidth / 2f - margin
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawColor(bgColor)
            // 对data的item的value进行求和
            val sum = data.sumOf { it.value.toDouble() }
            var factor: Float
            var start = 0f
            data.forEachIndexed { index, ringData ->
                factor = ringData.value / sum.toFloat()
                ringPaint.color = ringColor[index % ringColor.size]
                drawArc(
                    cx - radius,
                    cy - radius,
                    cx + radius,
                    cy + radius,
                    realStartAngle + start,
                    360f * factor,
                    false,
                    ringPaint
                )
                drawCenterLine(canvas, realStartAngle + start + 360f * factor / 2f)
                drawHorizontalLine(
                    canvas,
                    realStartAngle + start + 360f * factor / 2f,
                    ringData.text + " " + (factor * 100f).devimals(1) + "%",
                )

                start += 360f * factor
            }
//            drawCircle(cx, cy, radius - ringWidth / 2f, bgPaint)


            // canvas绘制多行文字
            canvas.save()
            canvas.translate(
                cx - centerTextPaint.measureText(centerText),
                cy - centerTextPaint.textSize
            )
            StaticLayout(
                centerText,
                centerTextPaint,
                300,
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                true
            ).draw(canvas)
            canvas.restore()
        }
    }

    private fun drawHorizontalLine(
        canvas: Canvas,
        angle: Float,
        text: String,
        len: Float = line1Len.toFloat()
    ) {
        val a = Math.toRadians(angle.toDouble()).toFloat()
        val startX = cx + (radius + ringWidth / 2f + len) * cos(a)
        val startY = cy + (radius + ringWidth / 2f + len) * sin(a)
        val dx = len * (if (startX >= cx) 1 else -1)
        canvas.drawLine(
            startX,
            startY,
            startX + dx,
            startY,
            textPaint
        )
        val dm = if (startX < cx) -textPaint.measureText(text) else 0f
        val dMargin = textMarginLine * (if (startX >= cx) 1 else -1) + dm
        canvas.drawText(text, startX + dx + dMargin, startY + textPaint.textSize / 3f, textPaint)

    }

    private fun drawCenterLine(canvas: Canvas, angle: Float, len: Float = line1Len.toFloat()) {
        val a = Math.toRadians(angle.toDouble()).toFloat()
        canvas.drawLine(
            cx + (radius + ringWidth / 2f) * cos(a),
            cy + (radius + ringWidth / 2f) * sin(a),
            cx + (radius + ringWidth / 2f + len) * cos(a),
            cy + (radius + ringWidth / 2f + len) * sin(a),
            textPaint
        )
    }

    public data class RingData(
        var value: Float,
        var text: String,
        var color: Int = 0,
    )
}