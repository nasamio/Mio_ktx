package com.mio.base.view.u

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mio.base.R
import com.mio.base.dp

open class UBaseView(
    context: Context,
    attributeSet: AttributeSet?,
    defaultCx: Double = 100.dp.toDouble(),
    defaultCy: Double = 100.dp.toDouble(),
    defaultMass: Double = 1.0,
    defaultVolume: Double = 1.0,
    defaultDensity: Double = 1.0,
    defaultVx: Double = 0.0,
    defaultVy: Double = 0.0,
    defaultAx: Double = 0.0,
    defaultAy: Double = 0.0,
    defaultE: Double = 1.0,
    defaultColor: Int = context.getColor(R.color.lcv_check),
) :
    View(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    // 质心坐标
    var cx: Double = 0.0
    var cy: Double = 0.0

    // 质量
    var mass: Double = 0.0

    // 体积
    var volume: Double = 0.0

    // 密度
    var density: Double = 0.0

    // 速度
    var vx: Double = 0.0
    var vy: Double = 0.0

    // 加速度
    var ax: Double = 0.0
    var ay: Double = 0.0

    // 碰撞系数
    var e: Double = 0.0

    var color: Int = 0

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.UBaseView)
        cx =
            typedArray.getDimensionPixelSize(R.styleable.UBaseView_cx, defaultCx.toInt()).toDouble()
        cy =
            typedArray.getDimensionPixelSize(R.styleable.UBaseView_cy, defaultCy.toInt()).toDouble()


        mass = typedArray.getFloat(R.styleable.UBaseView_mass, defaultMass.toFloat()).toDouble()
        volume =
            typedArray.getFloat(R.styleable.UBaseView_volume, defaultVolume.toFloat()).toDouble()
        density = typedArray.getFloat(R.styleable.UBaseView_density, defaultDensity.toFloat())
            .toDouble()
        vx = typedArray.getFloat(R.styleable.UBaseView_vx, defaultVx.toFloat()).toDouble()
        vy = typedArray.getFloat(R.styleable.UBaseView_vy, defaultVy.toFloat()).toDouble()
        ax = typedArray.getFloat(R.styleable.UBaseView_ax, defaultAx.toFloat()).toDouble()
        ay = typedArray.getFloat(R.styleable.UBaseView_ay, defaultAy.toFloat()).toDouble()
        e = typedArray.getFloat(R.styleable.UBaseView_e, defaultE.toFloat()).toDouble()

        color = typedArray.getColor(R.styleable.UBaseView_color, defaultColor)
        typedArray.recycle()
    }

    fun requirePaint(): Paint {
        val paint = Paint()
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        return paint
    }

    open fun checkCollision(others: MutableList<View>): UBaseView? {
        return null
    }

    /**
     * 每一帧触发一次
     */
    open fun update(time: Double) {
        vx += ax * time
        vy += ay * time
        cx += vx * time
        cy += vy * time
    }
}