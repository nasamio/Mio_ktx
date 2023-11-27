package com.mio.base.extension

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.mio.base.dp

fun Canvas.drawDialog(
    text: String,
    centerX: Float = -1f,
    centerY: Float = -1f,
    bgColor: Int = Color.parseColor("#73000000"),
    radius: Int = 5.dp,
    padding: Int = 10.dp,
    tSize: Int = 12.dp,
    xFactor: Float = 3f,// x轴矫正系数 正的往右边偏，负的往左边偏
    yFactor: Float = -1f,// y轴矫正系数 正的往下边偏，负的往上边偏
    jzFactor: Float = 5f,// 矫正系数
) {
    val bgPaint = Paint()
    bgPaint.apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = bgColor
    }
    val textPaint = TextPaint()
    textPaint.apply {
        isAntiAlias = true
        textSize = tSize.toFloat()
        color = Color.WHITE
    }

    val staticLayout = StaticLayout(
        text,
        textPaint,
        textPaint.measureText(text).toInt(),
        Layout.Alignment.ALIGN_CENTER,
        1.2f,
        1f,
        true
    )

    var cx = if (centerX < 0) width / 2f else centerX + xFactor * padding
    var cy = if (centerY < 0) height / 2f else centerY + yFactor * padding

    val dw = textPaint.measureText(text) + padding * 2
    // 获取text中有几行
    val dh = staticLayout.height + padding * 2

    if (cx - dw / 2 < 0) {
        // 左边超出了
        cx += jzFactor * padding
    } else if (cx + dw / 2 > width) {
        // 右边超出了
        cx -= jzFactor * padding
    }
    if (cy + dh / 2 > height) {
        // 下边超出了
        cy -= jzFactor * padding
    } else if (cy - dh / 2 < 0) {
        // 上边超出了
        cy += jzFactor * padding
    }

    drawRoundRect(
        cx - dw / 2,
        cy - dh / 2,
        cx + dw / 2,
        cy + dh / 2,
        radius.toFloat(),
        radius.toFloat(),
        bgPaint
    )

    save()
    translate(
        cx - textPaint.measureText(text) / 2f,
        cy - staticLayout.height / 2f
    )
    staticLayout.draw(this)

    restore()
}