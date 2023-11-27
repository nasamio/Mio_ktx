package com.mio.base.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.recyclerview.widget.RecyclerView
import com.mio.base.view.RvLinearLayoutManager
import kotlin.math.abs

object RvHelper {
    /**
     * 设置rv的item只能横向滚动 强制要求layout manager是RvLinearLayoutManager
     */
    fun setRvItemOnlyScrollHorizontal(view: View, recyclerView: RecyclerView) {
        // 实现横向拖动的时候 不让竖着的rv滚动

        val layoutManager = recyclerView.layoutManager as RvLinearLayoutManager
        view.setOnTouchListener(object : OnTouchListener {
            var lastX = 0f
            var lastY = 0f
            var dir = 0 // 0 未知 1 横向 2 竖向

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                // 判断x方向和y方向的偏移量 哪边先超过50px 就认为是哪个方向的滑动
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.x
                        lastY = event.y
                        layoutManager.isScrollEnabled = false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        when (dir) {
                            0 -> {
                                val dx = event.x - lastX
                                val dy = event.y - lastY
                                if (abs(dx) < 50 && abs(dy) < 50) {
                                    // 两个方向都不超过50px 不处理
                                    return false
                                }
                                if (abs(dx) > abs(dy)) {
                                    // 横向滑动
                                    layoutManager.canScrollHorizontal = true
                                    layoutManager.canScrollVertical = false
                                    dir = 1
                                } else {
                                    // 竖向滑动
                                    layoutManager.canScrollHorizontal = false
                                    layoutManager.canScrollVertical = true
                                    dir = 2
                                }
                            }

                            1 -> {
                                // 横向滑动
                                layoutManager.canScrollHorizontal = true
                                layoutManager.canScrollVertical = false
                                layoutManager.isScrollEnabled = false
                            }

                            2 -> {
                                // 竖向滑动
                                layoutManager.canScrollHorizontal = false
                                layoutManager.canScrollVertical = true
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        dir = 0
                        layoutManager.canScrollHorizontal = true
                        layoutManager.canScrollVertical = true
                        layoutManager.isScrollEnabled = true
                    }
                }
                return false
            }
        })
    }
}