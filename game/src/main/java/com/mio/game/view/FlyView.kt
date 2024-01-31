package com.mio.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mio.game.bean.Enemy
import com.mio.game.bean.Sprite
import com.mio.game.bean.User

class FlyView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    val sprites = mutableListOf<Sprite>().apply {
        add(Enemy().apply {
            name = "小强"
            x = 200
            y = 200
        })
    }
    val user by lazy {
        User().apply {
            name = "张三"
            x = 100
            y = 100
        }
    }

    private val userPaint by lazy {
        Paint().apply {
            color = context.getColor(com.mio.base.R.color.black_20)
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private val enemyPaint by lazy {
        Paint().apply {
            color = context.getColor(com.mio.base.R.color.black_80)
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    private val textPaint by lazy {
        Paint().apply {
            color = context.getColor(com.mio.base.R.color.black_80)
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    private val userWidth = 10f
    private val enemyWidth = 5f

    // 采用速率
    val updateSpeed = 16L

    // 是否在运行
    var isRunning = false
    var startTime = 0L

    // 运行时间
    var duration = 0L
    var cx = 0f
    var cy = 0f

    val random = java.util.Random()

    val level: Int = 2

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        startTime = System.currentTimeMillis()
        cx = w / 2f
        cy = h / 2f
        isRunning = true

        game()
    }

    fun game() {
        if (isRunning) {
            duration = System.currentTimeMillis() - startTime

            val shouldCount = duration / 1000L
            if (sprites.size - 1 < shouldCount) {
                sprites.add(Enemy().apply {
                    name = "小强"
                    x = width
                    y = random.nextInt(height)
                })
            }
            sprites.forEach {
                if (it is Enemy) {
                    if (com.mio.game.Utils.distance(
                            it.x.toFloat(), it.y.toFloat(),
                            user.x.toFloat(), user.y.toFloat()
                        ) < userWidth - enemyWidth
                    ) {
                        isRunning = false
                        invalidate()
                        return
                    }
                    if (it.x <= 0) it.x = width

                    it.x -= level
                }
            }

            invalidate()
        } else {
            showEnd()
        }

        postDelayed({ game() }, updateSpeed)
    }

    private fun showEnd() {
        // context.toast("游戏结束")
        duration = -1
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            for (sprite in sprites) {
                when (sprite) {
                    is Enemy -> {
                        canvas.drawCircle(
                            sprite.x.toFloat(),
                            sprite.y.toFloat(),
                            enemyWidth,
                            enemyPaint
                        )
                    }
                }
            }

            canvas.drawCircle(user.x.toFloat(), user.y.toFloat(), userWidth, userPaint)

            // 顶部中间绘制文字
            val topText = if (duration > 0) "游戏时间:${duration / 1000}秒"
            else "游戏结束,分数为:${sprites.size - 1}"
            canvas.drawText(
                topText,
                cx - textPaint.measureText(topText) / 2,
                textPaint.textSize * 2f,
                textPaint
            )
        }
    }
}