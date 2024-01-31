package com.mio.base.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.mio.base.R
import com.mio.base.Tag.TAG

@SuppressLint("UseCompatLoadingForDrawables")
class D3View(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    init {
//        setBackgroundColor(context.getColor(R.color.black_80))
        background = context.getDrawable(R.drawable.lock)
    }


    fun test(progress: Float) {

        rotation = progress * 10f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val cx = width / 2f
        val cy = height / 2f
        val x = event?.x
        val y = event?.y

        val max = 40f
        // 按下就模拟按压的效果
        when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            -> {
                val ry = (x!! - cx) / cx * max
                val rx = (y!! - cy) / cy * max
                rotationX = rx
                rotationY = -ry
                Log.d(TAG, "onTouchEvent: $rx , $ry")
            }

            MotionEvent.ACTION_UP -> {
                rotationX = 0f
                rotationY = 0f
            }
        }

        return super.onTouchEvent(event)
    }
}