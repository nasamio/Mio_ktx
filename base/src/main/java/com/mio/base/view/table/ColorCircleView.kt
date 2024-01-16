package com.mio.base.view.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

@SuppressLint("ViewConstructor")
class ColorCircleView(context: Context, val color: Int, var radius: Float = 0f) : View(context) {
    val paint by lazy {
        Paint().apply {
            color = this@ColorCircleView.color
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val cx = width / 2f
        val cy = height / 2f
        if (radius == 0f) {
            radius = minOf(width, height) / 4f
        }

        canvas?.drawCircle(cx, cy, radius, paint)
    }
}