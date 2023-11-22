package com.mio.base.extension

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.dp

/**
 * 纯色背景
 */
fun View.bg(color: Int) {
    background = colorDrawable(color)
}

/**
 * 圆角背景
 */
fun View.bg(color: Int, radius: Float, padding: Int = 5.dp) {
    background = roundedDrawable(color, radius)
    if (paddingStart == 0 && paddingTop == 0 && paddingEnd == 0 && paddingBottom == 0) {
        setPadding(padding, padding, padding, padding)
    }
}

fun View.bgOvalAndNormal(
    bgColor: Int,
    beforeColor: Int,
    radius: Int,
    factor: Float = 1f,
    offsetX: Int = 0,
    offsetY: Int = 0,
) {
    val ovalShape = OvalShape()
    val shapeDrawable = ShapeDrawable(ovalShape)
    shapeDrawable.apply {
        paint.color = beforeColor
    }

    val layerDrawable = LayerDrawable(
        arrayOf(
            colorDrawable(bgColor),
            shapeDrawable
        )
    )

//    layerDrawable.setLayerGravity(1, Gravity.TOP)
    layerDrawable.setLayerInsetLeft(1, (-radius / 5f * factor.toInt()).toInt() + offsetX)
    layerDrawable.setLayerInsetRight(1, (-radius / 5f * factor.toInt()).toInt() - offsetX)
    layerDrawable.setLayerInsetTop(1, -radius / 2f.toInt() - offsetY)
    layerDrawable.setLayerInsetBottom(1, height - radius / 2f.toInt() + offsetY)


    bg(layerDrawable)
}

fun RecyclerView.bgOvalAndNormal(
    bgColor: Int,
    beforeColor: Int,
    radius: Int,
    factor: Float = 1f,
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var totalDy = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            totalDy += dy

            bgOvalAndNormal(
                bgColor, beforeColor, radius, factor,
                offsetY = totalDy
            )
        }
    })
}

fun View.bg(drawable: Drawable) {
    background = drawable
}

/**
 * 纯色drawable
 */
fun View.colorDrawable(color: Int): Drawable = ColorDrawable(color)

/**
 * .9图drawable
 */
@SuppressLint("UseCompatLoadingForDrawables")
fun View.ninePatchDrawable(resId: Int): Drawable = context.getDrawable(resId)!!

fun View.shapeDrawable(): Drawable {
    val drawable = ShapeDrawable()
    drawable.shape =
        RoundRectShape(floatArrayOf(10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f), null, null)
    return drawable
}

fun View.roundedDrawable(color: Int, radius: Float): Drawable {
    val drawable = ShapeDrawable()
    drawable.apply {
        shape = RoundRectShape(
            floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius),
            null,
            null
        )
        paint.color = color
    }
    return drawable
}

fun View.layerListDrawable(drawable: Drawable, drawable2: Drawable): Drawable {
    val layerDrawable = LayerDrawable(arrayOf(drawable, drawable2))
    layerDrawable.setLayerInset(0, 0, 0, 0, 0)
    layerDrawable.setLayerInset(1, 10, 10, 10, 10)
    return layerDrawable
}